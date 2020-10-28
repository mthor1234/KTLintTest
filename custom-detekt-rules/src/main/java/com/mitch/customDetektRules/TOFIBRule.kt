/*
 * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.
 * TOFIBRuleTest.kt
 * This class has a group of functions that maps to Larcus Cloud API authentication.
 * @author Mitchell Thornton Feb 28, 2018
 */

package com.mitch.customDetektRules

import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_CREATION_DATE_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_FILE_NAME_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_UPDATE_LINE_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_AUTHOR
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_CREATION_DATE
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_DESCRIPTION
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_FIRST_LAST_NAME
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_UPDATE_LINE
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_YEAR_SPAN
import com.mitch.customDetektRules.Constants.Companion.DESCRIPTION_START_INDEX
import com.mitch.customDetektRules.Constants.Companion.MIN_NUMBER_OF_TOFIB_LINES
import com.mitch.customDetektRules.Constants.Companion.fileNameRegex
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Entity
import org.jetbrains.kotlin.psi.KtFile


class TOFIBRule : Rule() {

    lateinit var currentFile: KtFile
    lateinit var currentFileName: String



    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "This rule reports a file with an incorrect TOFIB.",
        Debt.FIVE_MINS
    )

    lateinit var tofib: TOFIB

    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)
        currentFile = file
        currentFileName = file.name

        System.out.println("Visiting File")

        crudeTOFIBCheck(file.text)


        // TODO: 10/27/20 Probably need a better check  

//        file.children.let {
//            System.out.println("Child Found")

//            if (it is PsiComment) {
//                System.out.println("Child Found: ${it.text}")
//                checkTOFIBForCorrectness(it.text)
//            }
//        }
    }


    fun crudeTOFIBCheck(tofibText: String){
        checkUpdateLine(tofibText)
        checkFileName(tofibText)
        checkDescription(tofibText)
//        checkAuthorLine(tofibText)

    }

//    fun checkTOFIBForCorrectness(tofibText: String) {
//        tofibText.split("\n").let { tofibLine: List<String> ->
//            for (lineNumber in 1..tofibLine.size) {
//                when {
//                    lineNumber == 1 -> checkUpdateLine(tofibLine.get(lineNumber))
//                    lineNumber == 2 -> parseFileName(tofibLine.get(lineNumber))
//                    lineNumber >= 3 -> parseDescription(tofibLine.get(lineNumber))
////                    4 -> parseAuthor(tofibLine.get(lineNumber))
//                }
//            }
//        }
//    }

    fun checkUpdateLine(tofibText: String) : Boolean {
        println("checkUpdateLine() : $tofibText")
        COPYRIGHT_REGEX_UPDATE_LINE.find(tofibText)?.let {
            parseYearRange(it.value)
            return true
        } ?: createIssue(COPYRIGHT_ISSUE_UPDATE_LINE_DOESNT_MATCH)

        return false
    }

    fun checkFileName(tofibText: String) : Boolean {

        println("checkFileName(): ${tofibText}")

        fileNameRegex(javaClass.simpleName).find(tofibText)?.let {
            parseYearRange(it.value)
            return true
        } ?: createIssue(COPYRIGHT_ISSUE_FILE_NAME_DOESNT_MATCH)
        return false
    }

    fun checkDescription(tofibText: String) : Boolean {
        println("checkDescription(): $tofibText")
        var authorStartLine: Int

        tofibText.split("\n").let { tofibLines: List<String> ->
            if(tofibLines.size >= MIN_NUMBER_OF_TOFIB_LINES) {
                // Description starts at line 3 but does not have a limit on how long it can be
                // Description falls in between FileName and Author
                // Author should occur - 1 from the end
                // Use index 3 to indicate starting index of the File Description
                // Use index .size - 2  to indicate end index of the File Description
                for (lineNumber in 3..tofibLines.size - 2) {

                    var line = tofibLines.get(lineNumber)

                    println("checkDescription(): \n lineNumber: $lineNumber\n" +
                        "${tofibLines.get(lineNumber)}")

                    // Ensure that the current line doesn't match other line REGEX's
                    if (!checkUpdateLine(line) &&
                        !checkFileName(line) &&
                        !checkAuthorLine(line)) {

                        COPYRIGHT_REGEX_DESCRIPTION.find(line)?.let {
                            println("checkDescription(): $COPYRIGHT_REGEX_DESCRIPTION")
                            return true
                        } ?: createIssue(COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH)
                        println("checkDescription(): One $COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH")
                        return false
                    }else{
                        createIssue(COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH)
                        println("checkDescription(): Two $COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH")
                        return false
                    }
                }
            }else{
                createIssue(COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES)
                println("checkDescription(): $COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES")
                return false
            }
        }
        return false
    }

    fun checkAuthorLine(tofibText: String) : Boolean {
        COPYRIGHT_REGEX_AUTHOR.find(tofibText)?.let {
            parseAuthor(it.value)
            parseCreationDate(it.value)
            return true
        } ?: createIssue(COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH)

        return false
    }

    fun parseYearRange(primaryCopyrightString: String): Array<String?> {
        var creationYear: String? = null
        var updatedYear: String? = null
        val years = arrayOfNulls<String>(2)

        COPYRIGHT_REGEX_YEAR_SPAN.find(primaryCopyrightString)?.let {
            val tokenizedYears = it.value.split("-")

            when (tokenizedYears.size) {
                0 -> createIssue(COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH)
                1 -> creationYear = tokenizedYears.get(0)
                2 -> {
                    creationYear = tokenizedYears.get(0)

                    // TODO: 10/26/20 If I add this to the diff only files, we should always set to the current year
                    updatedYear = tokenizedYears.get(1)
                }
            }
            years[0] = creationYear
            years[1] = updatedYear
        }
        return years
    }


    fun parseAuthor(tofibText: String): String? {
        return COPYRIGHT_REGEX_FIRST_LAST_NAME.find(tofibText)?.value ?: run {
            createIssue(COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH)
            return null
        }
    }

    fun parseCreationDate(tofibText: String): String? {
        return COPYRIGHT_REGEX_CREATION_DATE.find(tofibText)?.value ?: run {
            createIssue(COPYRIGHT_ISSUE_CREATION_DATE_DOESNT_MATCH)
            return null
        }
    }

    fun createIssue(message: String) {
        report(
            CodeSmell(
                issue, Entity.atPackageOrFirstDecl(currentFile),
                message = message
            )
        )
    }
}

