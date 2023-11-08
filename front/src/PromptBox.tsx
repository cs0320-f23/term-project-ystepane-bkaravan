import { useState } from "react";
import "./styles/main.css";
// import { REPLHistory } from "./REPLHistory";
import PromptInput from "./PromptInput";
/**
 * This is the main REPL class. It binds history and the REPLInput together.
 */

export default function PromptBox() {
  const [history, setHistory] = useState<string[][][]>([[[]]]); // history that records everything as a table
  const [file, setFile] = useState<string[][]>([[]]);

  return (
    <div className="promptbox" aria-label="Prompt box">
      {/* <hr>Search Queries</hr> */}
      <PromptInput
      // commands={history}
      // file={file}
      // setFile={setFile}
      // setHistory={setHistory}
      />
    </div>
  );
}
