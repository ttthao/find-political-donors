# Notes

1. Input
    - C00629618|N|TER|P|201701230300133512|15C|IND|PEREZ
    - Relevant fields
        - CMTE_ID: identifies the flier, which for our purposes is the recipient of this contribution
            - Ignore entry if empty
        - ZIP_CODE: zip code of the contributor (we only want the first five digits/characters)
            - If invalid/empty/lt5, count for medianvals_by_date, but ignore for medianvals_by_zip
        - TRANSACTION_DT: date of the transaction
            - If invalid date/empty/malformed, count for medianvals_by_zip, but not for medianvals_by_date
        - TRANSACTION_AMT: amount of the transaction
            - Ignore if empty
        - OTHER_ID: a field that denotes whether contribution came from a person or an entity
            - Count entries that have OTHER_ID set to empty

2. medianvals_by_zip.txt
    - Running median
    - Total dollar amount
    - Total number of contributions by recipient and zip code
    - 
3. medianvals_by_date.txt
    - median
    - total dollar amount
    - total number of contributions by recipient and date
4. Output files
    - Both files will have fields separated by '|'
    - medianvals_by_zip.txt
        - C00177436|30004|384|1|384
            - CMTE_ID | ZIP_CODE | running median | total # of transac. recieved by recipient from contributor's zip code streamed in so far | total amount of contirbutions received by recipient ''
            - running median of contributions received by recipient from the contributor's zip code streamed in so far
            - Median should be rounded to the whole dollar (round up >= $0.50)
        - Process input line by line, in small batches or all at once, but running median, total # of transac and total amount of contribution should be calculated with the input data streamed in so far.
    - medianvals_by_date.txt
        - C00177436|01312017|384|4|1382
            - CMTE_ID | TRANSCATION_DT | median | total # of transac on date | total amount of contributions by date
        - Doesn't depend on order of input file
        - Sort alphabetically by recipient, then chronologically by date
    