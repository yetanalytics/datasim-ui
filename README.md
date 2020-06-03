#  Data and Training Analytics Simulated Input Modeler (DATASIM) User Interface

## What is DATASIM?

The Data and Training Analytics Simulated Input Modeler is a research and development project designed to provide open source specifications and an open source reference model application for the purpose of generated simulated xAPI data at scale.

The purpose of DATASIM is to provide DoD distributed learning stakeholders the ability to simulate learning activities and generate the resulting xAPI statements at scale both in order to benchmark and stress-test the design of applications with the Total Learning Architecture and to provide stakeholders a way to evaluate the implementation of xAPI data design using the xAPI Profile specification. Ultimately, DATASIM can be used to support conformance testing of applications across the future learning ecosystem.

DATASIM is funded by the Advanced Distributed Learning Initiative at US DoD.

This documentation and repository refer to the user interface of DATASIM only. This runs as a fully browser-based implementation intended to be integrated with the API. For the Backend / API (which this interface is built upon) please see https://github.com/yetanalytics/datasim.

## Datasim UI

The UI Project is a CLJS frontend that allows a user to interact with the DATASIM simulator.

The user is presented with a REPL-like one-page experience to edit inputs and run DATASIM simulations.

The following actions are possible within the UI:

- Creating and editing all inputs
- Syntax Highlighting, linting, and validation
- Import all inputs from files and URL
- Export all inputs separately
- Export all inputs as a Simulation Specification
- Run a simulation and download resulting xAPI Statements
- Input LRS connection details
- Run a simulation and send resulting xAPI Statements to LRS
- Basic Security preventing server usage without credentials

## System Requirements

Java (JDK 8+, OpenJDK or Oracle)

Clojure (1.10.1+)

NodeJS (10.x+)

## Usage

To run in dev, run the following command from this directory:

    $ clojure -A:fig
    $ clojure -A:build-sass

or alternatively use the make command:

    $ make fig
    $ make build-sass

Once this is complete, the UI will be launched at http://localhost:9091, pointing to a DATASIM API at the default API location (http://localhost:9090/api/v1).

If you  the API is deployed somewhere else, please edit **line #1** of **dev.cljs.edn** to update the host and/or port before launching.

## License

DATASIM is licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for the full license text
