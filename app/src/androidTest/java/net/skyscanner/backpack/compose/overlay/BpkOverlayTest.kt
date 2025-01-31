/**
 * Backpack for Android - Skyscanner's Design System
 *
 * Copyright 2018 Skyscanner Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.skyscanner.backpack.compose.overlay

import net.skyscanner.backpack.compose.BpkSnapshotTest
import net.skyscanner.backpack.demo.compose.DefaultOverlaySample
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class BpkOverlayTest(private val overlayType: BpkOverlayType) : BpkSnapshotTest(listOf(overlayType)) {

    @Test
    fun default() = snap {
        DefaultOverlaySample(overlayType = overlayType)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} Screenshot")
        fun overlayTypes(): List<BpkOverlayType> =
            BpkOverlayType.entries.toList()
    }
}
