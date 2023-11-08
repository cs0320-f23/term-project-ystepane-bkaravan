import "./styles/main.css";
import {
  Dispatch,
  SetStateAction,
  isValidElement,
  useState,
  useEffect,
} from "react";
import { ControlledInput } from "./ControlledInput";

export default function PromptInput() {
  const [searchQ, setSearchQ] = useState<string>("");
  function handleSubmit(query: string) {
    console.log(query);
  }

  return (
    <div className="repl-input">
      <fieldset>
        <legend>Enter a search:</legend>
        <ControlledInput
          value={searchQ}
          setValue={setSearchQ}
          ariaLabel={"Command input"}
        />
      </fieldset>
      <button
        onClick={() => handleSubmit(searchQ)}
        //aria-label="input button"
      >
        Submit
      </button>
    </div>
  );
}
