package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.esgPython.EsgGeneralProducerRequest;
import com.merx.sdk.model.esgPython.EsgGeomGeneralRequest;
import com.merx.sdk.model.esgPython.GeojsonRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Catálogo de exemplos do recurso <b>esgPython</b> ({@code merx.esgPython()}).
 *
 * <p>Um método por endpoint público da SDK, com parâmetros de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class EsgPythonExamples {
    private final MerxClient merx;
    public EsgPythonExamples(MerxClient merx) { this.merx = merx; }

    /** Monta um GeoJSON FeatureCollection mínimo de exemplo para os endpoints de propriedade. */
    private GeojsonRequest sampleGeojson() {
        Map<String, Object> featureCollection = Map.of(
                "type", "FeatureCollection",
                "features", Collections.emptyList());
        return GeojsonRequest.builder().geojson(featureCollection).build();
    }

    /** Assentamentos do INCRA sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> incraPropertySettlement() {
        return merx.esgPython().incraPropertySettlement(sampleGeojson());
    }

    /** Converte um CAR em sua geometria (car2geom). */
    public Optional<Map<String, Object>> carToGeom(String car) {
        return merx.esgPython().carToGeom(car);
    }

    /** Dados cadastrais do produtor por CPF/CNPJ (socialId) e nome. */
    public Optional<Map<String, Object>> producerData(String socialId, String name) {
        return merx.esgPython().producerData(socialId, name);
    }

    /** Acesso à geometria ESG por identificador. */
    public Optional<Map<String, Object>> esgGeomAccess(String id) {
        return merx.esgPython().esgGeomAccess(id);
    }

    /** Análise ESG geral em lote sobre uma lista de geometrias. */
    public Optional<Map<String, Object>> esgGeomGeneral() {
        EsgGeomGeneralRequest request = EsgGeomGeneralRequest.builder()
                .geoms(List.of(Map.of("type", "Polygon", "coordinates", Collections.emptyList())))
                .build();
        return merx.esgPython().esgGeomGeneral(request);
    }

    /** Análise ESG geral em lote sobre uma lista de produtores. */
    public Optional<Map<String, Object>> esgGeneralProducer(String socialId, String name) {
        EsgGeneralProducerRequest request = EsgGeneralProducerRequest.builder()
                .producers(List.of(Map.of("socialId", socialId, "name", name)))
                .build();
        return merx.esgPython().esgGeneralProducer(request);
    }

    /** Terras indígenas FUNAI em domínio sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> funaiDominiaisProperty() {
        return merx.esgPython().funaiDominiaisProperty(sampleGeojson());
    }

    /** Terras indígenas FUNAI homologadas sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> funaiApprovedProperty() {
        return merx.esgPython().funaiApprovedProperty(sampleGeojson());
    }

    /** Interdições de terras indígenas FUNAI sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> funaiPropertyInterdiction() {
        return merx.esgPython().funaiPropertyInterdiction(sampleGeojson());
    }

    /** Terras indígenas FUNAI não homologadas sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> funaiNotApprovedProperty() {
        return merx.esgPython().funaiNotApprovedProperty(sampleGeojson());
    }

    /** Reservas indígenas FUNAI sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> funaiReservationProperty() {
        return merx.esgPython().funaiReservationProperty(sampleGeojson());
    }

    /** Autuações/embargos do IBAMA associados ao produtor (socialId, nome). */
    public List<Map<String, Object>> ibamaProducer(String socialId, String name) {
        return merx.esgPython().ibamaProducer(socialId, name);
    }

    /** Embargos do IBAMA sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> ibamaProperty() {
        return merx.esgPython().ibamaProperty(sampleGeojson());
    }

    /** Alertas de desmatamento INPE PRODES sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> inpeProdesProperty() {
        return merx.esgPython().inpeProdesProperty(sampleGeojson());
    }

    /** Moratória da soja associada ao produtor (socialId, nome). */
    public List<Map<String, Object>> producerMoratorium(String socialId, String name) {
        return merx.esgPython().producerMoratorium(socialId, name);
    }

    /** Moratória da soja sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> propertyMoratorium() {
        return merx.esgPython().propertyMoratorium(sampleGeojson());
    }

    /** Ações do MPT (trabalho escravo) associadas ao produtor (socialId, nome). */
    public List<Map<String, Object>> mptProducer(String socialId, String name) {
        return merx.esgPython().mptProducer(socialId, name);
    }

    /** Territórios quilombolas sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> quilombosProperty() {
        return merx.esgPython().quilombosProperty(sampleGeojson());
    }

    /** Registros da SEMA-MT associados ao produtor (socialId, nome). */
    public List<Map<String, Object>> semaMtProducer(String socialId, String name) {
        return merx.esgPython().semaMtProducer(socialId, name);
    }

    /** Embargos da SEMA-MT sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> semaMtProperty() {
        return merx.esgPython().semaMtProperty(sampleGeojson());
    }

    /** Unidades de conservação ICMBio sobre a propriedade (GeoJSON). */
    public Optional<Map<String, Object>> ucIcmbioProperty() {
        return merx.esgPython().ucIcmbioProperty(sampleGeojson());
    }
}
