import { ref } from "vue";

export const account = ref(null);
export const redirectPath = ref(null);
export const showForcedLoginModal = ref(false);

export function useAuth() {

    function setAccount(acc) {
        account.value = acc;
    }

    function setRedirectPath(path) {
        redirectPath.value = path;
    }

    function setShowForcedLoginModal(val) {
        showForcedLoginModal.value = val;
    }

    return {
        account,
        setAccount,
        redirectPath,
        setRedirectPath,
        showForcedLoginModal,
        setShowForcedLoginModal
    };
}
