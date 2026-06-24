package com.cooperativa.demo.core.journey;

import com.cooperativa.demo.core.sample.SampleData;
import com.cooperativa.demo.core.service.ComplianceService;
import com.cooperativa.demo.core.service.MarketDataService;
import com.cooperativa.demo.core.service.NegotiationService;
import com.cooperativa.demo.core.service.OnboardingService;
import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.farm.FarmResponse;
import com.merx.sdk.model.baseline.producer.ProducerResponse;
import com.merx.sdk.model.carbon.car.CarDto;
import com.merx.sdk.model.carbon.cbios.CbiosReportResponse;
import com.merx.sdk.model.carbon.eudr.SyncEuReportResponse;
import com.merx.sdk.model.carbon.socioambiental.SyncEsgReportResponse;
import com.merx.sdk.model.datafeed.QuoteRealtimeResponse;
import com.merx.sdk.model.order.delivery.DeliveryResponse;
import com.merx.sdk.model.productivity.Culture;
import com.merx.sdk.model.productivity.Productivity;

import java.util.List;
import java.util.UUID;

/**
 * Jornada de negócio completa da "Cooperativa Demo", ponta a ponta, usando os quatro serviços de
 * domínio. É a MESMA jornada executada pelos três exemplos (Java puro, Spring, Quarkus) — o que
 * muda entre eles é apenas como o {@code MerxClient} é construído e como a jornada é disparada.
 *
 * <p>Fluxo: cadastro (produtor → endereço → usuário → fazenda → armazém → local de entrega →
 * carteira) → compliance (CBIOS, EUDR, socioambiental, CAR) → negociação (compromisso de compra →
 * entrega → leitura → histórico → rastreabilidade) → dados de mercado (cotações, produtividade).
 *
 * <p>Cada etapa é resiliente (ver {@link JourneyReporter}): falhas pontuais no sandbox não abortam
 * a demonstração.
 */
public class CooperativaJourney {

    private final OnboardingService onboarding;
    private final ComplianceService compliance;
    private final NegotiationService negotiation;
    private final MarketDataService market;

    public CooperativaJourney(OnboardingService onboarding,
                              ComplianceService compliance,
                              NegotiationService negotiation,
                              MarketDataService market) {
        this.onboarding = onboarding;
        this.compliance = compliance;
        this.negotiation = negotiation;
        this.market = market;
    }

    /** Conveniência: monta os quatro serviços a partir de um {@link MerxClient}. */
    public static CooperativaJourney fromClient(MerxClient merx) {
        return new CooperativaJourney(
                new OnboardingService(merx),
                new ComplianceService(merx),
                new NegotiationService(merx),
                new MarketDataService(merx));
    }

    public JourneyResult run() {
        JourneyReporter r = new JourneyReporter();
        JourneyContext ctx = new JourneyContext();
        SampleData sample = new SampleData();
        r.note("Iniciando jornada (runId=" + sample.runId() + ", CAR=" + sample.car() + ")");

        // ---- Cadastro (baseline + identity) ----------------------------------------------------

        r.step("Cadastrar produtor (PJ)", () -> {
            ctx.producerId = onboarding.createProducer(sample.producer());
            return "producerId=" + ctx.producerId;
        });

        r.step("Adicionar endereço ao produtor", () -> {
            requireProducer(ctx);
            onboarding.addProducerAddress(ctx.producerId, sample.producerAddress());
            return "endereço vinculado";
        });

        r.step("Criar usuário do produtor (signatário)", () -> {
            requireProducer(ctx);
            ctx.producerUserId = onboarding.createProducerUser(ctx.producerId, sample.producerUser());
            return "producerUserId=" + ctx.producerUserId;
        });

        r.step("Registrar fazenda (com CAR)", () -> {
            requireProducer(ctx);
            ctx.farmId = onboarding.createFarm(ctx.producerId, sample.farm());
            return "farmId=" + ctx.farmId;
        });

        r.step("Registrar armazém", () -> {
            requireProducer(ctx);
            ctx.warehouseId = onboarding.createWarehouse(ctx.producerId, sample.warehouse());
            return "warehouseId=" + ctx.warehouseId;
        });

        r.step("Criar local de entrega", () -> {
            ctx.deliveryPlaceId = onboarding.createDeliveryPlace(sample.deliveryPlace());
            return "deliveryPlaceId=" + ctx.deliveryPlaceId;
        });

        r.step("Criar carteira de negociação", () -> {
            if (!ctx.has(ctx.producerUserId)) {
                throw new SkippedStepException("requer usuário do produtor (owner da carteira)");
            }
            ctx.walletId = onboarding.createWallet(sample.wallet(ctx.producerUserId.toString()));
            return "walletId=" + ctx.walletId;
        });

        r.step("Reler produtor (findById)", () -> {
            requireProducer(ctx);
            ProducerResponse p = onboarding.findProducer(ctx.producerId)
                    .orElseThrow(() -> new IllegalStateException("produtor recém-criado não encontrado"));
            return "tradingName=" + p.getTradingName() + ", doc=" + p.getSocialIdentity();
        });

        r.step("Listar fazendas do produtor", () -> {
            requireProducer(ctx);
            List<FarmResponse> farms = onboarding.listFarms(ctx.producerId);
            return farms.size() + " fazenda(s)";
        });

        // ---- Compliance / carbono --------------------------------------------------------------

        r.step("Gerar relatório CBIOS", () -> {
            CbiosReportResponse rep = compliance.cbiosReport(sample.cbiosReport());
            return "reportRequestId=" + rep.getReportRequestId() + ", status=" + rep.getStatus();
        });

        r.step("Gerar relatório EUDR", () -> {
            SyncEuReportResponse rep = compliance.eudrReport(sample.eudrReport());
            return "reportRequestId=" + rep.getReportRequestId() + ", status=" + rep.getEuStatusSummary();
        });

        r.step("Gerar relatório socioambiental", () -> {
            SyncEsgReportResponse rep = compliance.socioambientalReport(sample.socioambientalReport());
            return "reportRequestId=" + rep.getReportRequestId() + ", status=" + rep.getEsgStatusSummary();
        });

        r.step("Consultar CAR por código", () -> {
            CarDto car = compliance.findCar(sample.car());
            return car == null
                    ? "sem dados para o CAR"
                    : "município=" + car.getNomeMunicipio() + ", área=" + car.getNumArea();
        });

        // ---- Negociação (order) ----------------------------------------------------------------

        r.step("Criar compromisso de compra (COMPRA)", () -> {
            if (!ctx.has(ctx.producerId) || !ctx.has(ctx.deliveryPlaceId) || !ctx.has(ctx.walletId)) {
                throw new SkippedStepException("requer produtor, local de entrega e carteira");
            }
            UUID issuerId = ctx.has(ctx.producerUserId) ? ctx.producerUserId : ctx.producerId;
            ctx.commitmentId = negotiation.createCommitment(
                    sample.orderCommitment(ctx.producerId, issuerId, ctx.deliveryPlaceId, ctx.walletId));
            return "commitmentId=" + ctx.commitmentId;
        });

        r.step("Registrar entrega (delivery)", () -> {
            if (!ctx.has(ctx.commitmentId)) {
                throw new SkippedStepException("requer compromisso de compra");
            }
            DeliveryResponse d = negotiation.recordDelivery(sample.delivery(ctx.commitmentId));
            return "deliveryId=" + d.getId() + ", volume=" + d.getDeliveredVolume();
        });

        r.step("Reler compromisso (findById)", () -> {
            if (!ctx.has(ctx.commitmentId)) {
                throw new SkippedStepException("requer compromisso de compra");
            }
            return negotiation.findCommitment(ctx.commitmentId)
                    .map(c -> "status=" + c.getCommitmentStatus() + ", restante=" + c.getRemainingAmount())
                    .orElse("compromisso não encontrado");
        });

        r.step("Consultar histórico de volume entregue", () -> {
            if (!ctx.has(ctx.commitmentId)) {
                throw new SkippedStepException("requer compromisso de compra");
            }
            return negotiation.deliveredVolumeHistory(ctx.commitmentId)
                    .map(h -> "entregue=" + h.getDeliveredVolume() + " " + h.getUnitOfMeasurementCode())
                    .orElse("sem histórico");
        });

        r.step("Adicionar rastreabilidade (traceability)", () -> {
            if (!ctx.has(ctx.commitmentId) || !ctx.has(ctx.producerId) || !ctx.has(ctx.producerUserId)) {
                throw new SkippedStepException("requer compromisso, produtor e usuário");
            }
            UUID traceId = negotiation.addTraceability(ctx.commitmentId,
                    sample.traceability(ctx.producerId, ctx.producerUserId, ctx.farmId));
            return "traceabilityId=" + traceId;
        });

        // ---- Dados de mercado (somente leitura) ------------------------------------------------

        r.step("Consultar cotações em tempo real (DataFeed)", () -> {
            QuoteRealtimeResponse q = market.realtimeQuotes(List.of("SOJA", "MILHO"));
            int n = q.getInfo() == null ? 0 : q.getInfo().size();
            return "success=" + q.getSuccess() + ", " + n + " cotação(ões)";
        });

        r.step("Consultar produtividade por CAR/cultura", () -> {
            List<Productivity> list = market.productivityByCarAndCulture(sample.car(), Culture.SOY, "2024/2025");
            return list.size() + " registro(s) de produtividade";
        });

        return r.result(ctx);
    }

    private static void requireProducer(JourneyContext ctx) {
        if (!ctx.has(ctx.producerId)) {
            throw new SkippedStepException("requer produtor cadastrado");
        }
    }
}
