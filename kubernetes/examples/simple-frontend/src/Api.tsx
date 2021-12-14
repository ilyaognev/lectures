export const Api = {
    message: async (): Promise<String> => {
        return await fetch(`/backend/?person=docker`)
            .then((response) => response.text())
    }
}