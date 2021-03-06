/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.sleuth.instrument.web.client.feign;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.netflix.feign.FeignContext;

/**
 * Post processor that wraps Feign Context in its tracing representations.
 *
 * @author Marcin Grzejszczak
 *
 * @since 1.0.2
 */
final class FeignContextBeanPostProcessor implements BeanPostProcessor {

	private final BeanFactory beanFactory;
	private TraceFeignObjectWrapper traceFeignObjectWrapper;

	FeignContextBeanPostProcessor(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof FeignContext && !(bean instanceof TraceFeignContext)) {
			return new TraceFeignContext(getTraceFeignObjectWrapper(), (FeignContext) bean);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	private TraceFeignObjectWrapper getTraceFeignObjectWrapper() {
		if (this.traceFeignObjectWrapper == null) {
			this.traceFeignObjectWrapper = this.beanFactory.getBean(TraceFeignObjectWrapper.class);
		}
		return this.traceFeignObjectWrapper;
	}
}
