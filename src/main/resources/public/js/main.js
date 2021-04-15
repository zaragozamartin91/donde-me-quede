const EMPTYFN = () => {
}

const EMPTY_AUTOCOMPLETE = {on: EMPTYFN, autocomplete: EMPTYFN, val: EMPTYFN}
const EMPTY_TIMEPICKER = {timepicker: EMPTYFN}


$(document).ready(function () {
    $(".dropdown-trigger").dropdown();
    $('.sidenav').sidenav();

    let tp = $('.timepicker') || EMPTY_TIMEPICKER // initialize all timepickers
    tp.timepicker({twelveHour: false, defaultTime: '00:00'})


    // initialize create reading title autocomplete
    let crt = $('#create-reading-title') || EMPTY_AUTOCOMPLETE
    let searchTimeoutHandle = 0
    crt.on('input', function () {
        const titleRawVal = $(this).val()
        console.log("Title is ", titleRawVal)

        /* On every char input, clear search timeout */
        window.clearTimeout(searchTimeoutHandle)

        /* If input longer than 3 chars -> queue suggestion search */
        if (titleRawVal.length >= 3) {
            searchTimeoutHandle = window.setTimeout(function () {
                console.log("Looking suggestions for ", titleRawVal)
                const url = `/suggestions/${titleRawVal}`

                fetch(url).then(response => response.json()).then(data => {
                    console.log("Suggestions found: ", data)
                    const suggestions = {}
                    data.forEach(v => suggestions[v] = null)
                    crt.autocomplete({data: suggestions})
                    crt.autocomplete('open') // force autocomplete display suggestions
                })

            }, 1500)
        }
    })
})
