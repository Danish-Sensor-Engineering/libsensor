/*
 * Copyright 2020 Danish Sensor Engineering ApS
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

package biz.nellemann.libsensor;

public class TelegramHandler18Bit extends TelegramHandler {

    protected int convert(final int d1, final int d2, final int d3) {
        return (1024 * d3) + (4 * d2) + (d1 & 3);
    }

    protected boolean isHeader(int h) {
        if(h == 168 || h == 169 || h == 170 || h == 171)
            return true;

        return h == 84 || h == 85 || h == 86 || h == 87;
    }

    public String toString() {
        return "18bit";
    }
}
