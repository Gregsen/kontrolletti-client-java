/**
 * Copyright (C) 2015 Zalando SE (https://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.kontrolletti;

public interface KontrollettiOperations {

    /**
     * Accepts any valid (GIT) repository url and turns it into its normalized form. Normalized urls can be safely used
     * for comparisons.
     *
     * @param   repositoryUrl  a URL, e.g. "git@github.com:zalando/kontrolletti.git", or
     *                         "https://github.com/zalando/kontrolletti"
     *
     * @return  the normalized URL
     */
    String normalizeRepositoryUrl(String repositoryUrl);

    RepositoryResponse getRepository(String repositoryUrl);

}
