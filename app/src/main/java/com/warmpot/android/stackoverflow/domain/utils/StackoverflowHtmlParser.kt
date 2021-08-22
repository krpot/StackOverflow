package com.warmpot.android.stackoverflow.domain.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

private const val DIV_ID = "jsoup_html_container"

private const val HTML_TEMPLATE = """
<html lang="en">
    <link rel="shortcut icon" href="https://cdn.sstatic.net/Sites/stackoverflow/Img/favicon.ico?v=ec617d715196">
    <link rel="apple-touch-icon image_src" href="https://cdn.sstatic.net/Sites/stackoverflow/Img/apple-touch-icon.png?v=c78bd457575a">              

    <link rel="stylesheet" type="text/css" href="https://cdn.sstatic.net/Shared/stacks.css?v=73f2085cb668">
    <link rel="stylesheet" type="text/css" href="https://cdn.sstatic.net/Sites/stackoverflow/mobile.css?v=13358149f27e">
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1.0, minimum-scale=1.0">
    <body class="no-message-slide question-single" cz-shortcut-listen="true">
        <main class="snippet-hidden">
            <div itemprop="mainEntity" itemscope="" >
                <div class="question" id="question" data-questionid="68877836">
                    <header class="-summary">
                    </header>

                    <div class="s-prose js-post-body" id="$DIV_ID" itemprop="text">
                    </div><!-- / s-prose -->
                </div>
            </div>
            
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.2.0/styles/default.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.2.0/highlight.min.js"></script>
        <script>
            hljs.highlightAll();
        </script>
    </body>
</html>
"""

object StackoverflowHtmlParser {
    fun parseQuestionBodyHtml(body: String): String {
        if (body.isBlank()) return ""

        val bodyDoc: Document = Jsoup.parse(body)

        val document: Document = Jsoup.parse(HTML_TEMPLATE)
        val div: Element = document.getElementById(DIV_ID)
        div.appendChild(bodyDoc)
        return document.html().replace("<#root>", "")
    }
}
