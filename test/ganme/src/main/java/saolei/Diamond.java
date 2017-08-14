package saolei;

import java.awt.Button;
import java.util.Set;
// 每一个小方块类
public class Diamond extends Button {
    private Diamond[] diamonds;

    // 该小方块周围的八个方向上的小方块
    private Diamond east;
    private Diamond north;
    private Diamond northEast;
    private Diamond northWest;
    private Diamond south;
    private Diamond southEast;
    private Diamond southWest;
    private Diamond west;

    private boolean isBomb;// 是否是雷
    private boolean isChange;// 又没有被翻过
    private int no;// 产生的方块的编号

    // 持有所有小方块的引用,方便进行操作
    Diamond(Diamond[] diamonds) {
        this.diamonds = diamonds;
    }

    // 按键时方块发生改变
    boolean change() {
        this.isChange = true;// 说明已经翻过了
        if(isBomb) {// 触雷
            //this.setBackground(Color.red);
            return true;
        } else {// 不是雷,就显示周围雷的数目
            //this.setLabel(this.getNearBombNo() + "");
            this.setLabel(this.getNearBombNo() + "");
            //if(this.getNearBombNo() == 0) {
            //  this.moveon();
            //}
            return false;
        }
    }

    // 获得该小方块周围雷的数量
    int getNearBombNo() {
        int no = 0;
        if(this.northWest != null && this.northWest.isBomb) no++;
        if(this.north != null && this.north.isBomb) no++;
        if(this.northEast != null && this.northEast.isBomb) no++;
        if(this.east != null && this.east.isBomb) no++;
        if(this.southEast != null && this.southEast.isBomb) no++;
        if(this.south != null && this.south.isBomb) no++;
        if(this.southWest != null && this.southWest.isBomb) no++;
        if(this.west != null && this.west.isBomb) no++;

        return no;
    }

    // 获得该小方块周围的小方块
    Diamond getNearDimaond(int i) {
        int index = -1;
        switch (i) {
            case 1:// 1表示西北,2,表示北,以此类推
                index = no - 10;
                if(index < 1 || no == 19 || no == 28 || no == 37 || no == 46 || no == 55 || no == 64 || no == 73) {
                    return null;
                } else {
                    return diamonds[index];
                }
            case 2:
                index = no - 9;
                if(index < 1) {
                    return null;
                } else {
                    return diamonds[index];
                }
            case 3:
                index = no - 8;
                if(index < 1 || no == 9 || no == 18 || no == 27 || no == 36 || no == 45 || no == 54 || no == 63 || no == 72) {
                    return null;
                } else {
                    return diamonds[index];
                }
            case 4:
                index = no + 1;
                if(no == 9 || no == 18 || no == 27 || no == 36 || no == 45 || no == 54 || no == 63 || no == 72 || no == 81) {
                    return null;
                } else {
                    return diamonds[index];
                }
            case 5:
                index = no + 10;
                if(index >= 81 ||no == 9 || no == 18 || no == 27 || no == 36 || no == 45 || no == 54 || no == 63 || no == 72 || no == 81) {
                    return null;
                } else {
                    return diamonds[index];
                }
            case 6:
                index = no + 9;
                if(index > 81) {
                    return null;
                } else {
                    return diamonds[index];
                }
            case 7:
                index = no + 8;
                if(index >= 81 || no==1 || no == 10 || no == 19 || no == 28 || no == 37 || no == 46 || no == 55 || no == 64 || no == 73) {
                    return null;
                } else {
                    return diamonds[index];
                }
            case 8:
                index = no - 1;
                if(no==1 || no==10 || no == 19 || no == 28 || no == 37 || no == 46 || no == 55 || no == 64 || no == 73) {
                    return null;
                } else {
                    return diamonds[index];
                }
        }
        return null;
    }

    // 递归,set是用来装已经翻过的小方块的,不然会死循环,为什么用set,因为set是不重复的
    void moveon(Set<Diamond> set) {

        set.add(this);// 先把自己加上
        if(this.getNorthWest() != null && !this.getNorthWest().isBomb) {
            this.getNorthWest().change();

            if(this.getNorthWest().getNearBombNo() == 0) {
                if(!set.contains(this.getNorthWest()))
                    this.getNorthWest().moveon(set);
            }

            set.add(this.getNorthWest());
        }

        if(this.getNorth() != null && !this.getNorth().isBomb) {
            this.getNorth().change();
            if(this.getNorth().getNearBombNo() == 0) {
                if(!set.contains(this.getNorth()))
                    this.getNorth().moveon(set);
            }

            set.add(this.getNorth());
        }

        if(this.getNorthEast() != null && !this.getNorthEast().isBomb) {
            this.getNorthEast().change();
            if(this.getNorthEast().getNearBombNo() == 0) {
                if(!set.contains(this.getNorthEast()))
                    this.getNorthEast().moveon(set);
            }

            set.add(this.getNorthEast());
        }

        if(this.getEast() != null && !this.getEast().isBomb) {
            this.getEast().change();
            if(this.getEast().getNearBombNo() == 0) {
                if(!set.contains(this.getEast()))
                    this.getEast().moveon(set);
            }

            set.add(this.getEast());
        }

        if(this.getSouthEast() != null && !this.getSouthEast().isBomb) {
            this.getSouthEast().change();
            if(this.getSouthEast().getNearBombNo() == 0) {
                if(!set.contains(this.getSouthEast()))
                    this.getSouthEast().moveon(set);
            }

            set.add(this.getSouthEast());
        }

        if(this.getSouth() != null && !this.getSouth().isBomb) {
            this.getSouth().change();
            if(this.getSouth().getNearBombNo() == 0) {
                if(!set.contains(this.getSouth()))
                    this.getSouth().moveon(set);
            }

            set.add(this.getSouth());
        }

        if(this.getSouthWest() != null && !this.getSouthWest().isBomb) {
            this.getSouthWest().change();
            if(this.getSouthWest().getNearBombNo() == 0) {
                if(!set.contains(this.getSouthWest()))
                    this.getSouthWest().moveon(set);
            }

            set.add(this.getSouthWest());
        }

        if(this.getWest() != null && !this.getWest().isBomb) {
            this.getWest().change();
            if(this.getWest().getNearBombNo() == 0) {
                if(!set.contains(this.getWest()))
                    this.getWest().moveon(set);
            }

            set.add(this.getWest());
        }
    }

    /*public Diamond[] getDiamonds() {
        return diamonds;
    }*/

    Diamond getEast() {
        return east;
    }

    public int getNo() {
        return no;
    }

    Diamond getNorth() {
        return north;
    }

    Diamond getNorthEast() {
        return northEast;
    }

    Diamond getNorthWest() {
        return northWest;
    }

    Diamond getSouth() {
        return south;
    }

    Diamond getSouthEast() {
        return southEast;
    }

    Diamond getSouthWest() {
        return southWest;
    }

    Diamond getWest() {
        return west;
    }

    boolean isBomb() {
        return isBomb;
    }

    boolean isChange() {
        return isChange;
    }

    void setBomb() {
        this.isBomb = true;
    }

    public void setChange(boolean isChange) {
        this.isChange = isChange;
    }

    public void setDiamonds(Diamond[] diamonds) {
        this.diamonds = diamonds;
    }

    void setEast(Diamond east) {
        this.east = east;
    }

    void setNo(int no) {
        this.no = no;
    }

    void setNorth(Diamond north) {
        this.north = north;
    }

    void setNorthEast(Diamond northEast) {
        this.northEast = northEast;
    }

    void setNorthWest(Diamond northWest) {
        this.northWest = northWest;
    }

    void setSouth(Diamond south) {
        this.south = south;
    }

    void setSouthEast(Diamond southEast) {
        this.southEast = southEast;
    }

    void setSouthWest(Diamond southWest) {
        this.southWest = southWest;
    }

    void setWest(Diamond west) {
        this.west = west;
    }

}