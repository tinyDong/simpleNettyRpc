package com.xd.connecter.client;

import com.xd.dto.RequestDto;
import com.xd.dto.ResponseDto;
import org.omg.CORBA.Request;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RpcFuture implements Future<Object>{

    private RequestDto request;
    private ResponseDto response;
    private long startTime;
    private ReentrantLock lock =new ReentrantLock();
    private Condition condition=lock.newCondition();

    public RpcFuture(RequestDto request){
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return true;
    }

    public Object get() throws InterruptedException, ExecutionException {
        if (response==null){
            try {
                lock.lock();
                condition.await();
            } finally {
                lock.unlock();
            }
        }
        return this.response.getResult();
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public void process(ResponseDto response) {

        try {
            lock.lock();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
        this.response=response;
    }
}
