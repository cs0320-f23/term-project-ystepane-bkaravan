# maps-bkaravan-ibrauns

Front now has the following structure:
In app, we have components MapBox(pretty much from the gearup),
and PromptBox(equivalent to the REPL component previously)
In promptbox, we just have a nested component called PromptInput
PromptInput then just handles the search bar together with
ControlledInput, just like we did in Repl. The functionality is
very minimal but we can build from here I think. To play around
with CSS, go to styles/main.css, and there, you will have record-like names. Those names represent whatever div has the className with same name. For example, if PromptInput has a div with className="promptinput", its css box will start with .promptinput
