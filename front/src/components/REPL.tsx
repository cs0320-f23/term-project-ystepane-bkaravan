import { useState } from "react";
import "../styles/main.css";
import { REPLHistory } from "./REPLHistory";
import { REPLInput } from "./REPLInput";
import  MapBox from "./MapBox"
/**
 * This is the main REPL class. It binds history and the REPLInput together.
 */

export default function REPL() {
  const [history, setHistory] = useState<string[][][]>([[[]]]); // history that records everything as a table
  const [file, setFile] = useState<string[][]>([[]]);

  return (
    <div className="repl" aria-label="High component">
      <MapBox />
      <hr></hr>
      <REPLHistory
      commands = {history}/>
      <REPLInput
        commands={history}
        file={file}
        setFile={setFile}
        setHistory={setHistory}
      />
    </div>
  );
}
