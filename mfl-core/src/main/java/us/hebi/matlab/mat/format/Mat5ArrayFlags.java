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

import us.hebi.matlab.mat.format.Mat5Serializable.Mat5Attributes;
import us.hebi.matlab.mat.types.*;

import static us.hebi.matlab.mat.util.Preconditions.*;

/**
 * Utilities to deal with the int[2] array flags that are
 * at the beginning of each array.
 *
 * @author Florian Enner
 * @since 14 Sep 2018
 */
class Mat5ArrayFlags {

    static int[] forArray(boolean global, Array array) {
        if (array instanceof Mat5Attributes) {
            Mat5Attributes attr = ((Mat5Attributes) array);
            return create(array.getType(), global, attr.isLogical(), attr.isComplex(), attr.getNzMax());
        }
        checkArgument(!(array instanceof Sparse), "Sparse matrices must implement Mat5Attributes");

        boolean logical = array instanceof Matrix && ((Matrix) array).isLogical();
        boolean complex = array instanceof Matrix && ((Matrix) array).isComplex();
        return Mat5ArrayFlags.create(array.getType(), global, logical, complex, 0);
    }

    /**
     * Opaques may show up as a different public type, e.g., Object. This method ignores
     * the public type and forces the array type to be Opaque.
     */
    static int[] forOpaque(boolean global, Opaque opaque) {
        return create(MatlabType.Opaque, false, false, false, 0);
    }

    private static int[] create(MatlabType type, boolean global, boolean logical, boolean complex, int nzMax) {
        int attributes = type.id() & FLAG_MASK_TYPE_ID;
        if (logical) attributes |= FLAG_BIT_LOGICAL;
        if (global) attributes |= FLAG_BIT_GLOBAL;
        if (complex) attributes |= FLAG_BIT_COMPLEX;
        return new int[]{attributes, nzMax};
    }

    static MatlabType getType(int[] arrayFlags) {
        return MatlabType.fromId(arrayFlags[0] & FLAG_MASK_TYPE_ID);
    }

    static boolean isComplex(int[] arrayFlags) {
        return (arrayFlags[0] & FLAG_BIT_COMPLEX) != 0;
    }

    static boolean isGlobal(int[] arrayFlags) {
        return (arrayFlags[0] & FLAG_BIT_GLOBAL) != 0;
    }

    static boolean isLogical(int[] arrayFlags) {
        return (arrayFlags[0] & FLAG_BIT_LOGICAL) != 0;
    }

    static int getNzMax(int[] arrayFlags) {
        return arrayFlags[1];
    }

    private static final int FLAG_MASK_TYPE_ID = 0xff;
    private static final int FLAG_BIT_LOGICAL = 1 << 9;
    private static final int FLAG_BIT_GLOBAL = 1 << 10;
    private static final int FLAG_BIT_COMPLEX = 1 << 11;

}
