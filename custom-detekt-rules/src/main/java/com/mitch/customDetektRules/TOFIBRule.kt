/**
 * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.
 * TOFIBRule.kt
 * This class has a group of functions that maps to Larcus Cloud API authentication.
 * @author Mitchell Thornton Feb 28, 2018
 */

package com.mitch.customDetektRules

import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_CREATION_DATE_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_FILE_NAME_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_TOFIB_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_UPDATE_LINE_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_AUTHOR
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_AUTHOR_LINE
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_CREATION_DATE
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_DESCRIPTION
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_FIRST_LAST_NAME
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_TOFIB
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_UPDATE_LINE
import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT_REGEX_YEAR_SPAN
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
    lateinit var tofib: TOFIB

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "This rule reports a file with an incorrect TOFIB.",
        Debt.FIVE_MINS
    )


    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)
        currentFile = file
        currentFileName = file.name
        crudeTOFIBCheck(file.text)
    }


    fun crudeTOFIBCheck(tofibText: String): Boolean{
        println("crudeTOFIBCheck(): $tofibText")
        if(COPYRIGHT_REGEX_TOFIB.find(tofibText) == null){
            createIssue(COPYRIGHT_ISSUE_TOFIB_DOESNT_MATCH)
            return false
        }
        return true
    }


    fun checkTOFIBForCorrectness(tofibText: String) {
        tofibText.split("\n").let { tofibLine: List<String> ->

            if (tofibLine.size > MIN_NUMBER_OF_TOFIB_LINES) {

                // TODO: 10/29/20 Need to fix the total number of lines, by finding when the TOFIB ends... This is just temporary, to see how the rest of the code is working
                for (lineNumber in 1..tofibLine.size - 2) {
                    when {
                        lineNumber == 1 -> {
                            if (!checkUpdateLine(tofibLine.get(lineNumber))) {
                                break
                            }
                        }

                        lineNumber == 2 -> {
                            if (!checkFileName(tofibLine.get(lineNumber))) {
                                break
                            }
                        }

                        lineNumber >= 3 && lineNumber != tofibLine.size - 2 -> {
                            if (!checkIfValidDescriptionLine(tofibLine.get(lineNumber))) {
                                break
                            }
                        }
                        lineNumber == tofibLine.size - 2 -> {
                            if (checkAuthorLine(tofibLine.get(lineNumber))) {
                                break
                            }
                        }
                        else -> {
                            createIssue("Something wrong with the TOFIB")
                            break
                        }
                    }
                }
            } else {
                createIssue(COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES)
            }
        }
    }

    fun checkUpdateLine(tofibText: String): Boolean {
        println("checkUpdateLine() : \n$tofibText")
        COPYRIGHT_REGEX_UPDATE_LINE.find(tofibText)?.let {
            println("checkUpdateLine() Success")
            return true
        } ?: createIssue(COPYRIGHT_ISSUE_UPDATE_LINE_DOESNT_MATCH)

        return false
    }

    fun checkFileName(tofibText: String): Boolean {

        println("checkFileName(): \n${tofibText}")

        fileNameRegex(javaClass.simpleName).find(tofibText)?.let {
            println("checkFileName() Success")
            return true
        } ?: createIssue(COPYRIGHT_ISSUE_FILE_NAME_DOESNT_MATCH)
        println("checkFileName() FAILURE")
        return false
    }

    fun checkTOFIBForDescription(tofibText: String): Boolean {
        println("checkTOFIBForDescription(): \n$tofibText")

        tofibText.split("\n").let { tofibLines: List<String> ->
            if (tofibLines.size >= MIN_NUMBER_OF_TOFIB_LINES) {
                // Description starts at line 3 but does not have a limit on how long it can be
                // Description falls in between FileName and Author
                // Author should occur - 1 from the end
                // Use index 3 to indicate starting index of the File Description
                // Use index .size - 2  to indicate end index of the File Description
                for (lineNumber in 3..tofibLines.size - 3) {

                    var line = tofibLines.get(lineNumber)

                    println(
                        "checkTOFIBForDescription(): \n lineNumber: $lineNumber\n" +
                            "${line}"
                    )
                    // If there is an issue found, stop the loop & return false
                    if (!checkIfValidDescriptionLine(line)) {
                        return false
                    }
                }
                // Entire Description has been processed with no issues, return true
                return true
            } else {
                createIssue(COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES)
                println("checkDescription(): $COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES")
                return false
            }
        }
        return false
    }


    fun checkIfValidDescriptionLine(descriptionLine: String): Boolean {

        println("checkIfValidDescriptionLine(): \n$descriptionLine")

        COPYRIGHT_REGEX_DESCRIPTION.find(descriptionLine)?.let {
            println("checkIfValidDescriptionLine(): SUCCESS")
            return true
        } ?: createIssue(COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH)

        println("checkIfValidDescriptionLine(): One $COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH")
        return false
    }

    fun checkAuthorLine(tofibText: String): Boolean {
        println("checkAuthorLine(): \n$tofibText")

        COPYRIGHT_REGEX_AUTHOR_LINE.find(tofibText)?.let {
            println("checkAuthorLine() Success")

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
        } ?: run {
            println("parseYearRange() : Couldn't find year range!")
            createIssue(COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH)
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

