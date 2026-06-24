package com.cooperativa.demo.core.service;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.address.CreateAddressRequest;
import com.merx.sdk.model.baseline.deliveryPlace.CreateDeliveryPlaceRequest;
import com.merx.sdk.model.baseline.farm.CreateFarmRequest;
import com.merx.sdk.model.baseline.farm.FarmResponse;
import com.merx.sdk.model.baseline.producer.CreateProducerRequest;
import com.merx.sdk.model.baseline.producer.ProducerResponse;
import com.merx.sdk.model.baseline.wallet.CreateWalletRequest;
import com.merx.sdk.model.baseline.warehouse.CreateWarehouseRequest;
import com.merx.sdk.model.identity.producer.CreateProducerUserRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Cadastro (baseline + identity): produtor, endereço, usuário, fazenda, armazém, local de
 * entrega e carteira. Cada método é um wrapper fino sobre um resource client do SDK — é aqui que
 * o integrador vê como invocar cada endpoint.
 *
 * <p>Classe agnóstica de framework: recebe o {@link MerxClient} por construtor. O exemplo Java
 * puro instancia direto; Spring/Quarkus a expõem como bean gerenciado.
 */
public class OnboardingService {

    private final MerxClient merx;

    public OnboardingService(MerxClient merx) {
        this.merx = merx;
    }

    public UUID createProducer(CreateProducerRequest request) {
        return merx.producers().create(request).getId();
    }

    public void addProducerAddress(UUID producerId, CreateAddressRequest request) {
        merx.producers().addAddress(producerId, request);
    }

    public UUID createProducerUser(UUID producerId, CreateProducerUserRequest request) {
        return merx.producerUsers().create(producerId, request).getId();
    }

    public UUID createFarm(UUID producerId, CreateFarmRequest request) {
        return merx.farms().create(producerId, request).getId();
    }

    public UUID createWarehouse(UUID producerId, CreateWarehouseRequest request) {
        return merx.warehouses().create(producerId, request).getId();
    }

    public UUID createDeliveryPlace(CreateDeliveryPlaceRequest request) {
        return merx.deliveryPlaces().create(request).getId();
    }

    public UUID createWallet(CreateWalletRequest request) {
        return merx.wallets().create(request).getId();
    }

    public Optional<ProducerResponse> findProducer(UUID producerId) {
        return merx.producers().findById(producerId);
    }

    public List<FarmResponse> listFarms(UUID producerId) {
        return merx.farms().findByProducer(producerId);
    }
}
