export const Api = {
    message: async (): Promise<String> => {
        return await fetch(`/backend/`)
            .then((response) => response.text())
    }
}