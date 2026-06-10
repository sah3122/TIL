package dean.ai

import kotlin.math.max

object LeetCode {
    fun romanToInt(s: String): Int {
        val charArray = s.toCharArray()
        var result = 0
        var i = 0
        while (i < s.length) {
            result += if (convertInt(charArray[i]) <  convertInt(charArray[i + 1])) {
                convertInt(charArray[i + 1]) - convertInt(charArray[i])
            } else {
                convertInt(charArray[i])
            }
            i++
        }
        return result
    }

    private fun convertInt(char: Char): Int {
        if (char == 'I') return 1
        if (char == 'V') return 5
        if (char == 'X') return 10
        if (char == 'L') return 50
        if (char == 'C') return 100
        if (char == 'D') return 500
        if (char == 'M') return 1000
        return 0
    }

    fun longestCommonPrefix(strs: Array<String>): String {
        var prefix = ""
        for (i in 0 until strs[0].length) {
            val char = strs[0].toCharArray()[i]
            for (str in strs) {
                if (i < str.length || str[i] != char) {
                    return prefix
                }
            }
            prefix += char
        }
        return prefix
    }

    fun climbStairs(n: Int): Int {
        val dp: IntArray = IntArray(n)
        dp[0] = 1
        dp[1] = 2

        recurClimbStairs(2, n, dp)
        return dp[n-1]
    }

    fun recurClimbStairs(index: Int, n: Int, dp: IntArray) {
        if (index ==  n) {
            return
        }
        dp[index] = dp[index - 1] + dp[index - 2]
        recurClimbStairs(index + 1, n, dp)
    }

    fun lengthOfLastWord(s: String): Int {
        var lastWordCount = 0
        for (i in s.length - 1 downTo 0) {
            if (lastWordCount != 0 && s[i] == ' ') return lastWordCount
            if (s[i] == ' ') continue
            else lastWordCount++
        }
        return lastWordCount
    }

    fun isPalindrome(s: String): Boolean {
        val lowercase = s.filter { it.isLetter() }.lowercase().toCharArray()
        var start = 0
        var end = lowercase.size - 1

        while (start <= end) {
            if (lowercase[start] != lowercase[end]) return false

            start++
            end--
        }
        return true
    }

    fun reverseWords(s: String): String {

        return s.trim().split(" ").reversed().filter { it.isNotBlank() }.joinToString(" ").toString()
    }

    fun rotate(nums: IntArray, k: Int): Unit {
//        val n = nums.size / k
//        nums.
    }

    fun strStr(haystack: String, needle: String): Int {
        if (haystack == needle) return 0

        haystack.forEachIndexed { i, c ->
            if (c == needle[0]) {
                if (i + needle.length < haystack.length - 1 && haystack.substring(i, i + needle.length) == needle) {
                    return i
                }
                return@forEachIndexed
            }
        }
        return -1
    }

    fun canJump(nums: IntArray): Boolean {
        for (i in 0 .. nums.size) {

        }
        return false
    }

    fun majorityElement(nums: IntArray): Int {
        val groupBy = nums.groupBy { it }

        return groupBy.maxBy { it.value.size }.key
    }

    fun isSubsequence(s: String, t: String): Boolean {
        var i = 0
        for (j in 0 .. t.length) {
            if (s[i] == t[j]) {
                i++
            }
        }

        return if (i == s.length) true
        else false
    }

    fun rob(nums: IntArray): Int {
        val dp = IntArray(nums.size)
        dp[0] = nums[0]
        dp[1] = nums[1]

        for (i in 2 .. nums.size - 1) {
            if (i == 2) {
                dp[i] = dp[i - 2] + nums[i]
                continue
            }
            dp[i] = max(dp[i - 2] + nums[i], dp[i - 3] + nums[i])
        }

        return dp.max()
    }

    fun letterCombinations(digits: String): List<String> {
        fun backtracking(value: String, index: Int, digits: String, answers: MutableSet<String>) {
            if (value.length == digits.length) {
                answers.add(value)
                return
            }

            val current = digitToAlpha(digits[index])

            for (i in 0 until current.length) {
                backtracking(value + current[i], index + 1, digits, answers)
            }
        }

        var answers = mutableSetOf<String>()
        val currentLetter = digitToAlpha(digits[0])

        backtracking("", 0, digits, answers)

        return answers.toList()
    }


    private fun digitToAlpha(digit: Char): String {
        return when (digit) {
            '2' -> "abc"
            '3' -> "def"
            '4' -> "ghi"
            '5' -> "jkl"
            '6' -> "mno"
            '7' -> "pqrs"
            '8' -> "tuv"
            '9' -> "wxyz"
            else -> ""
        }
    }
}