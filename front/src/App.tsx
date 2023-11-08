import React, { useEffect, useState } from "react";
import "./App.css";
import MapBox from "./MapBox";
import PromptBox from "./PromptBox";

// REMEMBER TO PUT YOUR API KEY IN A FOLDER THAT IS GITIGNORED!!
// (for instance, /src/private/api_key.tsx)
// import {API_KEY} from "./private/api_key"

function App() {
  return (
    <div className="App">
      {/* <p className="App-header">
        <h1>Urban Studies</h1>
      </p> */}
      {/* TODO: PLACE MAP CONTENT HERE */}
      <MapBox />
      <PromptBox />
    </div>
  );
}

export default App;
