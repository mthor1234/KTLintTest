/*
 * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.
 * CloudAuth.kt
 * This class has a group of functions that maps to Larcus Cloud API authentication.
 * @author Mitchell Thornton Feb 28, 2018
 */

package com.mitch.customDetektRules

/**
 * Created by MJ Thornton
 * 10/27/20
 *
 */
class Constants {

    private constructor()

    companion object {

        const val COPYRIGHT =
        "/*\n" +
            "* Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.\n" +
            "* CloudAuth.kt\n" +
            " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
            "* @author Mitchell Thornton Feb 28, 2018\n" +
            "*/"

        // REGEX
        val COPYRIGHT_REGEX_UPDATE_LINE =
            Regex("(( \\* Copyright Mitchell James Thornton, Inc.)" +
                "[\\s]([0-9]{4})([-]([0-9]{4}))?)(. All rights reserved.)")
        val COPYRIGHT_REGEX_DESCRIPTION =
            Regex("(^ \\* (.{20,}))") // Starts with  * and has at least 20 characters

        // Author
        val COPYRIGHT_REGEX_AUTHOR = Regex("((^ \\* @author )([A-Z][a-z]+([ ][A-Z][a-z]+)))")
        val COPYRIGHT_REGEX_FIRST_LAST_NAME = Regex("([A-Z][a-z]+([ ]?[A-Z][a-z]+))")
        val COPYRIGHT_REGEX_CREATION_DATE = Regex("([A-Z][a-z]{2} [0-2]?[0-9], [0-9]{4})")
        val COPYRIGHT_REGEX_AUTHOR_LINE =
            Regex( "${COPYRIGHT_REGEX_AUTHOR.pattern} ${COPYRIGHT_REGEX_CREATION_DATE}")


        val COPYRIGHT_REGEX_YEAR_SPAN = Regex("([0-9]{4})([-]([0-9]{4}))?")

        // ISSUES
        const val COPYRIGHT_ISSUE_UPDATE_LINE_DOESNT_MATCH =
            "Primary TOFIB Does not Match Expected. \n"+
                "Ex: \"* Copyright Mitchell James Thornton, Inc....\""

        const val COPYRIGHT_ISSUE_FILE_NAME_DOESNT_MATCH =
            "File name Does not Match Expected. \n" +
                "Ex: \"* TestFile.kt\""

        const val COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH =
            "Description is not found or formatted properly. \n" +
                "Ex: \"* This is a test description. \""

        const val COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH =
            "@author line is not found or formatted properly: \n" +
                "Ex: \"* @author Mitchell Thornton Feb 28, 2018\""

        const val COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH =
            "Something wrong with the year-range format. Please double check"

        const val COPYRIGHT_ISSUE_CREATION_DATE_DOESNT_MATCH =
            "Initial creation date is not found or formatted properly"

        const val COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES =
            "The TOFIB Does Not Have 5 or More Lines"

        // Expected line where the description is supposed to start
        const val DESCRIPTION_START_INDEX = 3
        const val MIN_NUMBER_OF_TOFIB_LINES = 5



        fun fileNameRegex(className: String): Regex {
            return Regex("^\\s(\\* ${className}\\.kt)")
            // ^\s(\* ${className}\.kt)


        }

    }
}
