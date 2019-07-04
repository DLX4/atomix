/*
 * Copyright 2019-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.server.impl;

import java.util.function.BiFunction;

import io.atomix.api.primitive.Name;
import io.atomix.server.protocol.ProtocolClient;
import io.atomix.service.PrimitiveService;
import io.atomix.service.protocol.ServiceId;
import io.atomix.service.proxy.ServiceProxy;

/**
 * Primitive executor.
 */
public class PrimitiveFactory<P extends ServiceProxy> {
    private final ProtocolClient client;
    private final PrimitiveService.Type serviceType;
    private final BiFunction<ServiceId, ProtocolClient, P> primitiveFactory;

    public PrimitiveFactory(
        ProtocolClient client,
        PrimitiveService.Type serviceType,
        BiFunction<ServiceId, ProtocolClient, P> primitiveFactory) {
        this.client = client;
        this.serviceType = serviceType;
        this.primitiveFactory = primitiveFactory;
    }

    /**
     * Returns the primitive with the given ID.
     *
     * @param id the primitive ID
     * @return the primitive
     */
    public P getPrimitive(Name id) {
        return primitiveFactory.apply(getServiceId(id), client);
    }

    /**
     * Returns the service ID for the given primitive ID.
     *
     * @param id the primitive ID
     * @return the service ID
     */
    private ServiceId getServiceId(Name id) {
        return ServiceId.newBuilder()
            .setName(id.getName())
            .setNamespace(id.getNamespace())
            .setType(serviceType.name())
            .build();
    }
}