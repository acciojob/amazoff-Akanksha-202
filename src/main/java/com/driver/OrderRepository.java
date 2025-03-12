package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

import static java.lang.Math.max;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        orderMap.put(order.getId(),order);
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerMap.put(partner.getId(),partner);
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            partnerToOrderMap.putIfAbsent(partnerId,new HashSet<>());
            partnerToOrderMap.get(partnerId).add(orderId);

            //update order count of partner
            DeliveryPartner partner = partnerMap.get(partnerId);
            partner.setNumberOfOrders(partnerToOrderMap.get(partnerId).size());

            //assign partner to this order
            orderToPartnerMap.put(orderId,partnerId);
        }

    }

    public Order findOrderById(String orderId){
        // your code here
        if(orderMap.containsKey(orderId)){
            return orderMap.get(orderId);
        }
        return null;
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        if(partnerMap.containsKey(partnerId)){
            return partnerMap.get(partnerId);
        }
        return null;
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        if(partnerMap.containsKey(partnerId)){
            return partnerMap.get(partnerId).getNumberOfOrders();
        }
        return 0;
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        List<String> orderOfPartner = new ArrayList<>();
        if(partnerToOrderMap.containsKey(partnerId)){
            orderOfPartner.addAll(partnerToOrderMap.get(partnerId));
        }

        //throw exception partner id doesnt exists
        return orderOfPartner;

    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        partnerMap.remove(partnerId);

        HashSet<String> orderIdsToUnassign = partnerToOrderMap.remove(partnerId);

        if(orderIdsToUnassign != null && !orderIdsToUnassign.isEmpty()){
            for(String orderId : orderIdsToUnassign){
                orderToPartnerMap.remove(orderId);
            }
        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        orderMap.remove(orderId);

        String partnerId = orderToPartnerMap.remove(orderId);

        if (partnerId != null && partnerToOrderMap.containsKey(partnerId)) {
            partnerToOrderMap.get(partnerId).remove(orderId);

            // If the partner has no more assigned orders, remove the entry
            if (partnerToOrderMap.get(partnerId).isEmpty()) {
                partnerToOrderMap.remove(partnerId);
            }
        }

    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        int countOfUnassignedOrders = 0;
        for(String orderId : orderMap.keySet()){
            if(!orderToPartnerMap.containsKey(orderId)){
                countOfUnassignedOrders++;
            }
        }

        return countOfUnassignedOrders;
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here

        int count = 0;

        //1. Convert string time to int time
        int hh = Integer.parseInt(timeString.substring(0,2));
        int mm = Integer.parseInt(timeString.substring(3));
        int givenTime = hh*60 + mm;
        //2. get orderId from partner to Order map
        HashSet<String> orderIdList = partnerToOrderMap.get(partnerId);
        //3. get delivery time from order map
        //4. compare with given time
        //5. if given time is greater than delivery time
        //6. count++
        for(String x : orderIdList){
            int deliveryTime = orderMap.get(x).getDeliveryTime();
            if(givenTime > deliveryTime){
                count++;
            }
        }

        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM

        if(!partnerMap.containsKey(partnerId)){
            return "No Partner exist with this partnerId";
        }

        if (!partnerToOrderMap.containsKey(partnerId) || partnerToOrderMap.get(partnerId).isEmpty()) {
            return "No orders assigned";
        }

        int lastDeliveryTime = 0;

        HashSet<String> orderIdList = partnerToOrderMap.get(partnerId);
        for(String x : orderIdList){
            int deliveryTime = orderMap.get(x).getDeliveryTime();
            lastDeliveryTime = max(lastDeliveryTime,deliveryTime);
        }


        return String.format("%02d:%02d",lastDeliveryTime/60,lastDeliveryTime%60);
    }
}