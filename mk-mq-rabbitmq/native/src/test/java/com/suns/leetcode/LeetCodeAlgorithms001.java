/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns <br>
 *
 * @author mk <br>
 * Date:2018-12-13 15:26 <br>
 */

package com.suns.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: LeetCodeAlgorithms <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-13 15:26 <br>
 * @version
 */
public class LeetCodeAlgorithms001 {

    class Solution {
        public int[] twoSum(int[] nums, int target) {
            int []result = new int[2];
            for(int i=0;i<nums.length;i++){
                for(int j=i+1;j<nums.length;j++){
                    if(nums[i]+nums[j] == target){
                        result[0]=i;
                        result[1]=j;
                        System.out.println("["+nums[i]+","+nums[j]+"]");
                        return result;
                    }
                }
            }
            return null;
        }

        public int[] twoSum2(int[] nums, int target) {
            Map<Integer,Integer> map = new HashMap<>();
            for(int i=0;i<nums.length;i++){
                map.put(nums[i],i);
            }
            for(int i=0;i<nums.length;i++){
                int complement = target - nums[i];
                if(map.containsKey(complement)){
                    System.out.println("["+i+","+map.get(complement)+"]");
                    return new int []{i, map.get(complement)};
                }
            }

            throw new IllegalArgumentException("no two sum solution");
        }

        public int[] twoSum3(int[] nums, int target) {
            Map<Integer,Integer> map = new HashMap<>();
            for(int i=0;i<nums.length;i++){
                int complement = target - nums[i];
                if(map.containsKey(complement)){
                    System.out.println("["+i+","+map.get(complement)+"]");
                    System.out.println(map);
                    return new int []{i, map.get(complement)};
                }
                map.put(nums[i],i);
            }

            throw new IllegalArgumentException("no two sum solution");
        }
    }

}
