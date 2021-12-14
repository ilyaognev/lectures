import React, {FC, useEffect, useState} from "react";
import "./App.css";
import {Api} from "./Api";

const App: FC = () => {
    const [message, setMessage] = useState<String>();
    useEffect(() => {
        Api.message().then(message => setMessage(message))
    }, [])

    return (
        <div className="App">
            <header className="App-header">
                <p>{!message ? "Loading..." : message}</p>
            </header>
        </div>
    );
}

export default App;
