package com.example.android_demo_application

import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.entities.ShouyeItem
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun testGetArticleList(){
        var list:List<ShouyeItem> = HttpUtils.getLists(0);
        assertEquals(list.size,10);
    }
    @Test
    fun testLogin(){
        assertEquals(HttpUtils.login("lifangzheng","12345"),"");
    }
}