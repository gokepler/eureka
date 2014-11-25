/*
 * Copyright 2014 Netflix, Inc.
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

package com.netflix.eureka2.client.metric;

import com.netflix.eureka2.client.channel.InterestChannelMetrics;
import com.netflix.eureka2.client.channel.RegistrationChannelMetrics;
import com.netflix.eureka2.transport.base.MessageConnectionMetrics;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Tomasz Bak
 */
public class EurekaClientMetricFactory {

    private static EurekaClientMetricFactory INSTANCE;

    private final EurekaClientRegistryMetrics registryMetrics;

    private final MessageConnectionMetrics registrationServerConnectionMetrics;

    private final MessageConnectionMetrics discoveryServerConnectionMetrics;

    private final RegistrationChannelMetrics registrationChannelMetrics;

    private final InterestChannelMetrics interestChannelMetrics;

    @Inject
    public EurekaClientMetricFactory(EurekaClientRegistryMetrics registryMetrics,
                                     @Named("registration") MessageConnectionMetrics registrationServerConnectionMetrics,
                                     @Named("discovery") MessageConnectionMetrics discoveryServerConnectionMetrics,
                                     RegistrationChannelMetrics registrationChannelMetrics,
                                     InterestChannelMetrics interestChannelMetrics) {
        this.registryMetrics = registryMetrics;
        this.registrationServerConnectionMetrics = registrationServerConnectionMetrics;
        this.discoveryServerConnectionMetrics = discoveryServerConnectionMetrics;
        this.registrationChannelMetrics = registrationChannelMetrics;
        this.interestChannelMetrics = interestChannelMetrics;
    }

    public EurekaClientRegistryMetrics getRegistryMetrics() {
        return registryMetrics;
    }

    public MessageConnectionMetrics getRegistrationServerConnectionMetrics() {
        return registrationServerConnectionMetrics;
    }

    public MessageConnectionMetrics getDiscoveryServerConnectionMetrics() {
        return discoveryServerConnectionMetrics;
    }

    public RegistrationChannelMetrics getRegistrationChannelMetrics() {
        return registrationChannelMetrics;
    }

    public InterestChannelMetrics getInterestChannelMetrics() {
        return interestChannelMetrics;
    }

    public static EurekaClientMetricFactory clientMetrics() {
        if (INSTANCE == null) {
            synchronized (EurekaClientMetricFactory.class) {
                EurekaClientRegistryMetrics registryMetrics = new EurekaClientRegistryMetrics("client");
                registryMetrics.bindMetrics();

                MessageConnectionMetrics registrationServerConnectionMetrics = new MessageConnectionMetrics("clientRegistration");
                registrationServerConnectionMetrics.bindMetrics();

                MessageConnectionMetrics discoveryServerConnectionMetrics = new MessageConnectionMetrics("clientDiscovery");
                discoveryServerConnectionMetrics.bindMetrics();

                RegistrationChannelMetrics registrationChannelMetrics = new RegistrationChannelMetrics();
                registrationChannelMetrics.bindMetrics();

                InterestChannelMetrics interestChannelMetrics = new InterestChannelMetrics();
                interestChannelMetrics.bindMetrics();

                INSTANCE = new EurekaClientMetricFactory(registryMetrics, registrationServerConnectionMetrics,
                        discoveryServerConnectionMetrics, registrationChannelMetrics, interestChannelMetrics);
            }
        }
        return INSTANCE;
    }
}
