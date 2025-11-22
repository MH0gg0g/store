#include <bits/stdc++.h>
using namespace std;

typedef vector <int> vi;
typedef vector <vi> vvi;
const int maxNum = 1e5+5;

#############-------DFS-------##################

vvi edges (maxNum);
vi visited (maxNum);

void dfs (int node) {
    visited(node) = 1;

    for(int neighbor : edges[node]) {
        if(!visited(node)) {
            dfs(neighbor);
        }
    }
}

#############-------PREFIX-------##############

vector<int> arr;
vector<int> prefix (maxNum);

for(int i = 0; i < arr.size(); i++) prefix[i+1] =  prefix[i] + arr[i];


###############################################



int main()
{
    cout << "Hello world!" << endl;
    return 0;
}
