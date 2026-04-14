package dean.ai

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
}