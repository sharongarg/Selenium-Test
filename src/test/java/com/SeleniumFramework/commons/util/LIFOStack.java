package com.SeleniumFramework.commons.util;
import java.util.List;
import java.util.ArrayList;
public class LIFOStack<E> 

{

    private List<E> stack;

    public LIFOStack(int SIZE) 
    {
        stack = new ArrayList<E>(SIZE);
    }

    public void push(E i) 
    {

       stack.add(0,i);
     }

     public E pop() 
     { 
        if(!stack.isEmpty()){
           E i= stack.get(0);
           stack.remove(0);
           return i;
        }
		return null; 
     }
     
     public String getElementsWithoutPopOut() 
     { 
    	String elementId="";;
    	for (int i= stack.size()-1; i >=0; i--){
    		if(elementId==""){
    			elementId = stack.get(i)+"";
    		}else{
    			elementId = elementId + "_" + stack.get(i);
    		}
    		
    	}
		return elementId; 
     }
         
     
     


 }