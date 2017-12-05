package com.paulz.carinsurance.model.wrapper;

import com.paulz.carinsurance.model.SalesmanCount;
import com.paulz.carinsurance.model.Team;

import java.util.ArrayList;
import java.util.List;

public class SalesmanCountWraper implements BeanWraper<SalesmanCount>{
	
	/**
	 * 
	 */
    public List<SalesmanCount> list; //  当前页面所有的beans  order

    public int total=1;//页码总数


    @Override
    public int getItemsCount(){
    	return list==null?0:list.size();
    }
    
    @Override
    public List<SalesmanCount> getItems(){
    	if(list==null){
            list=new ArrayList<>();
    	}
    	return list;
    }
    
    @Override
    public int getTotalPage(){
    	return total;
    }
    
}
