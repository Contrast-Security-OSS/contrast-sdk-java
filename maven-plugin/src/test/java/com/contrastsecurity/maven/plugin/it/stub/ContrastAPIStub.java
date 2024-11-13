package com.contrastsecurity.maven.plugin.it.stub;

/*-
 * #%L
 * Contrast Maven Plugin
 * %%
 * Copyright (C) 2021 Contrast Security, Inc.
 * %%
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
 * #L%
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Provides a JUnit test with a {@link ContrastAPI} stub for testing. Starts the {@code ContrastAPI}
 * instance before starting the test, and handles gracefully terminating the Contrast API instance
 * at the conclusion of the test.
 *
 * <pre>
 *   &#64;ContrastAPIStub
 *   &#64;Test
 *   public void test(final ContrastAPI contrast) { ... }
 * </pre>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ContrastAPIStubExtension.class)
public @interface ContrastAPIStub {}
