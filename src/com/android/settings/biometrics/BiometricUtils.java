/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.settings.biometrics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.internal.widget.LockPatternUtils;
import com.android.settings.password.ChooseLockSettingsHelper;

/**
 * Common biometric utilities.
 */
public class BiometricUtils {
    /**
     * Given the result from confirming or choosing a credential, request Gatekeeper to generate
     * a HardwareAuthToken with the Gatekeeper Password together with a biometric challenge.
     *
     * @param context Caller's context
     * @param result The onActivityResult intent from ChooseLock* or ConfirmLock*
     * @param userId User ID that the credential/biometric operation applies to
     * @param challenge Unique biometric challenge from FingerprintManager/FaceManager
     * @return
     */
    public static byte[] requestGatekeeperHat(Context context, Intent result, int userId,
            long challenge) {
        final byte[] gkPassword = result.getByteArrayExtra(
                ChooseLockSettingsHelper.EXTRA_KEY_GK_PW);
        if (gkPassword == null) {
            throw new IllegalStateException("Gatekeeper Password is null!!");
        }

        final LockPatternUtils utils = new LockPatternUtils(context);
        return utils.verifyGatekeeperPassword(gkPassword, challenge, userId).getGatekeeperHAT();
    }

    public static boolean containsGatekeeperPassword(Intent data) {
        if (data == null) {
            return false;
        }
        return data.getByteArrayExtra(ChooseLockSettingsHelper.EXTRA_KEY_GK_PW) != null;
    }
}