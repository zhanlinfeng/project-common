package com.deepthink.common;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {
    interface inf {
        void getA();
    }

    class TestClass implements inf {

        @Override
        public void getA() {

        }
    }

    class TestInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    public void test() {
        Class[] infs = new Class[]{inf.class};
        Object o = Proxy.newProxyInstance(TestClass.class.getClassLoader(), infs, new TestInvocationHandler());

    }

    public static void main(String[] args) {

    }
}
