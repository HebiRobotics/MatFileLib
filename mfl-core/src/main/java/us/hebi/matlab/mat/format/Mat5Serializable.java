/*-
 * #%L
 * MAT File Library
 * %%
 * Copyright (C) 2018 HEBI Robotics
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

package us.hebi.matlab.mat.format;

import us.hebi.matlab.mat.types.Sink;

import java.io.IOException;

/**
 * Marker interface for all parts (e.g. array/header/data components) that can be serialized using
 * the MAT5 format. Arrays that do not implement this interface may be rejected by the Mat5Writer.
 * <p>
 * The serialized bytes include any necessary tags (e.g. matrix tag) and padding
 *
 * @author Florian Enner
 * @since 29 Aug 2018
 */
public interface Mat5Serializable {

    /**
     * @param name name
     * @return Number of serialized bytes including the Matrix tag
     */
    int getMat5Size(String name);

    /**
     * Serializes this array as a MatFile entry including all tags. The MAT5 file format
     * encodes some root-level information such as name or the global flag within each
     * array, so they need to be passed in as meta information.
     *
     * @param name     the name of the root variable. Always empty string for nested arrays
     * @param isGlobal true if the root variable is global. Always false for nested arrays.
     * @param sink     the output target
     * @throws IOException for any issues due to writing to the sink
     */
    void writeMat5(String name, boolean isGlobal, Sink sink) throws IOException;

    /**
     * Interface for serializing custom array classes that need to
     * have control over array flags/attributes without implementing
     * the entire interface (e.g. sparse::getNzMax() or matrix::isComplex())
     */
    interface Mat5Attributes {

        boolean isLogical();

        boolean isComplex();

        int getNzMax();

    }

}
