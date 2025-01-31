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

import systems.danger.kotlin.*
import java.io.File

danger(args) {

    val createdFiles = git.createdFiles
    val modifiedFiles = git.modifiedFiles
    val allSourceFiles = modifiedFiles + createdFiles

    onGitHub {
        val isTrivial = pullRequest.title.contains("#trivial")

        fun shouldContainLicensingInformation(filePath: String): Boolean =
            filePath.matches(Regex("[^\\s]+(\\.(?i)(js|sh|kt|java))$")) && !filePath.contains("build/")

        // If any files were created, the BpkComponentUsageDetector should have been updated.
        val usageDetector = modifiedFiles.contains(
            "backpack-lint/src/main/java/net/skyscanner/backpack/lint/check/BpkComponentUsageDetector.kt",
        )
        val packageFilesCreated = createdFiles.any { it.startsWith("Backpack/src/main/java") }
        if (packageFilesCreated && !usageDetector && !isTrivial) {
            warn("One or more package files were created, but `BpkComponentUsageDetector.kt` wasn't updated.")
        }

        // If any files were created, the BpkComposeComponentUsageDetector should have been updated.
        val composeUsageDetector = modifiedFiles.contains(
            "backpack-lint/src/main/java/net/skyscanner/backpack/lint/check/BpkComposeComponentUsageDetector.kt",
        )
        val composePackageFilesCreated = createdFiles.any { it.startsWith("backpack-compose/src/main/kotlin") }
        if (composePackageFilesCreated && !composeUsageDetector && !isTrivial) {
            warn("One or more package files were created, but `BpkComposeComponentUsageDetector.kt` wasn't updated.")
        }

        // If any screenshots were created, README should"ve updated.
        val screenshotsCreated = createdFiles.any { it.startsWith("docs") && it.contains("/screenshots/") }
        if (screenshotsCreated) {
            val readmeUpdated = allSourceFiles.any { it.startsWith("docs") && it.endsWith("README.md") }
            if (!readmeUpdated) {
                warn("One or more screenshot created, but `README.md` wasn't updated. Please include screenshots in README")
            }
        }

        // New files should include the Backpack license heading.
        val unlicensedFiles = createdFiles.filter { filePath ->
            if (shouldContainLicensingInformation(filePath)) {
                val fileContent = File(filePath).readText(Charsets.UTF_8)
                !fileContent.contains("Licensed under the Apache License, Version 2.0 (the \"License\")")
            } else {
                false
            }
        }
        if (unlicensedFiles.size > 0) {
            fail("These new files do not include the license heading: ${unlicensedFiles.joinToString(", ")}")
        }
    }
}
