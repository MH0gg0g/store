#include <bits/stdc++.h>
using namespace std;

typedef long long ll;
typedef pair <int, int> ii;
typedef vector <ii> vii;

    int countDistinctIntegers(vector<int>& nums) {
        unordered_set<int> unSet (nums.begin(), nums.end());
        int sizze = nums.size();
        int newNum = 0;
        for(int i = 0; i < sizze; i++) {
            while(nums[i]) {
                newNum = newNum * 10 + nums[i] % 10;
                nums[i] /= 10;
            }
            unSet.insert(newNum);
            newNum = 0;
        }
        for(int i : unSet)
            cout << i << "   ";

        return unSet.size();
    }
int main()
{


        vector <int> points {1,13,10,12,31};
        countDistinctIntegers(points);



}
