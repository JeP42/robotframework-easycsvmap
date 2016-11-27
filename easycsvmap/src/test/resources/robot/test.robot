*** Settings ***
Library           Remote    http://localhost:8270/RobotEasyCsv

*** Variables ***

*** Test Cases ***
Sample Test Case
    Log    ${EXECDIR}
    Parse Csv From File    ${EXECDIR}/../com/github/jep42/easycsvmap/header-0-five-lines.csv    0
    Add Row    aaa    bbb    ccc
    Save Csv To File    ${EXECDIR}/updated.csv