import { PublicClientApplication } from "@azure/msal-browser";
import { msalConfig } from "./msalConfig.js";
import { useAuth } from "@/composables/useAuth";
import router from "@/router";

export const msalInstance = new PublicClientApplication(msalConfig);

let isInitialized = false;

export const initializeMsal = async () => {
    if (isInitialized) return msalInstance; // singleton
    isInitialized = true;

    try {
        const auth = useAuth();

        await msalInstance.initialize();

        const response = await msalInstance.handleRedirectPromise();

        if (response?.account) {
            msalInstance.setActiveAccount(response.account);
            auth.setAccount(response.account);

            const redirectPath = auth.redirectPath.value;
            if (redirectPath) {
                auth.setRedirectPath(null);
                auth.setShowForcedLoginModal(false);
                await router.push(redirectPath);
            }
        } else {
            const accounts = msalInstance.getAllAccounts();
            if (accounts.length > 0) {
                msalInstance.setActiveAccount(accounts[0]);
                auth.setAccount(accounts[0]);
            }
        }
    } catch (err) {
        console.error("MSAL init error:", err);
    }

    return msalInstance;
};
