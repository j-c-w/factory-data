/**
 * Created by Jackson on 11/11/2014.
 */
$(document).ready(function() {
    $(".lazyList").jscroll({
        padding: 60,
        nextSelector: 'a.jscroll-next:last',
        loadingHtml: '<p class="center"><img src="/assets/images/loading.gif" alt="Loading"/></p>' // The HTML to show at the bottom of the content while loading the next set.
    });
});