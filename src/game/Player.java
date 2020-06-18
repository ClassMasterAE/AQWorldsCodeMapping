package game;

//Pseudo-code project mapping in game behavior and functions. Python for quick functions and java for OOP constructs

import game.Server;
import javafx.util.Pair;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Locale;

public class Player {
    //Essential values
    private int playerID = 0;   //playerID to fetch name, items in inventory and so on from parent server. Assigned at login
    private Server server = zhoom;    //Currently server being played on (zhoom, safiria). Assigned at "pick server" screen
    private Locable currentLocation;
    private Mapable currentMap;
    private Itemable equipedItems;
    private Selectable selectedMonster = null;
    private Timestamp autoAttackCooldown = new Timestamp(Server.currentTimeMillis());
    private Timestamp skillCooldown = new Timestamp(Server.currentTimeMillis());
    private boolean isAttacking = false;

    public Player (Server server) {            //Constructor for loading a map
        this.currentMap = this.server.loadMap();      //returns("Room", "Area");  Map room number and location area (top,mid,left,right, bottom)
        this.currentLocation = this.server.loadLoc();     //returns(x,y);   Room loc in x,y grid format
        this.equipedItems = this.server.loadCharacter();     //returns character loadout
    }

    private void loadedObjects () {  //Disregard, inaccurate and oversimplified function. Only to show functionality.
        Mapable currentMap = new server.map(mapID); //Loads map
        Selectable loadMonstersOnMap = new server.monster(mapID); //Loads monsters on map with location
        if (this.map.room.containsNewObject())  //Scans current room for new unloaded objects
            Selectable newPlayer = new Monster(playerID);  //Creates new monster for each new player
    }

    public void runPlayer () {    //Allows for Player inputs once loaded into a map
        while (true) {
            switch (input) {      //Tests case of input
                case 1:  //Case auto-attack is detected
                    ArrayList<Selectable> enemiesInRange = this.server.enemiesInGridBaseAttackRange(this.currentLocation);  //Finds enemies within attack range of base attack range
                    if (!enemiesInRange.isEmpty()){   //Check is there are anyone within range
                        attackMonsterEvent(enemiesInRange,1);  //Runs skill associated game events
                    }
                    else //Chat message if no monster within range
                        System.out.println("No target selected!");

                case 2,3,4,5:  //Case skill is detected
                    ArrayList<Selectable> enemiesInRange = this.server.enemiesInGridSkillAttackRange(this.currentLocation);  //Finds enemies within attack range of skill
                    if (skillCooldown.compareTo(serverCurrectTimestamp) > this.player.haste) {  //Checks cooldown buffer for base usage of skills, (0.5sec +- x) ish
                        if (!enemiesInRange.isEmpty()){       //Check is there are anyone within range
                            for (byte i = 0; i<enemiesInRange.length(); i++ ){  //Runs attack on every monster the skill hits
                                if(i<skill.AoE)
                                    attackMonster(enemiesInRange.get(i),input);  //Attacks monster after list priority
                                else
                                    break;
                            }
                        }
                        else   //Chat message if no monster within range
                            System.out.println("Ability "+skill.name+" is not ready yet");
                    }


                case mouseLeftClick:
                    if (currentMap.rangeX.contains(click.location[0]) && currentMap.rangeY.contains(click.location[1]))  //Checks if click is in bounds
                        if(click.location.contains(monster) && this.selectedMonster != monster)   //Checks if the click is on selectable (monster)
                            this.selectedMonster = this.selectMonster(monster.playerID);  //Selects clicked on object
                        else
                            this.attackMonster(this.selectedMonster,1);  //If already selected initiates attackEvent
                    this.movePlayer(click.location);
                case "other":
                    //Do other input associated functionality
                default:
                    if (isAttacking && autoAttackCooldown.compareTo(serverCurrectTimestamp) > 2) {  //Checks if it has been 2 seconds since last auto-attack
                        ArrayList<Selectable> enemiesInRange = this.server.enemiesInGridBaseAttackRange(this.currentLocation);  //Finds enemies within attack range of base attack range
                        if (enemiesInRange.contains(this.selectedMonster)) {    //Attacks enemy if found in range
                            autoAttackCooldown = new Timestamp(Server.currentTimeMillis()); //Updates new "cooldown" for auto-attack
                            attackMonster(this.selectedMonster, 1);  //Attacks monster, sends to server
                        }
                    }
            }
        }
    }

    private void attackMonsterEvent (ArrayList<Selectable> enemiesInRange, byte skill) {  //Event for skills that allow for Dashing
        isAttacking = true;
        if (this.selectedMonster == null){  //Selects target if no is already selected
            this.selectMonster(enemiesInRange.get(0));  //Selects first player/monster in list. "Users in your area"
        }
        if (enemiesInRange.contains(this.selectedMonster))    //If enemy within range this.player attacks monster
            if (isAttacking && autoAttackCooldown.compareTo(serverCurrectTimestamp) > 2) {  //Checks if it has been 2 seconds since last auto-attack
                ArrayList<Selectable> enemiesInRange = this.server.enemiesInGridBaseAttackRange(this.currentLocation);  //Finds enemies within attack range of base attack range
                if (enemiesInRange.contains(this.selectedMonster)) {    //Attacks enemy if found in range
                    autoAttackCooldown = new Timestamp(Server.currentTimeMillis()); //Updates new "cooldown" for auto-attack
                    attackMonster(this.selectedMonster, 1);  //Attacks monster, sends to server
                }
            }
        else if (this.selectedMonster.currentLocation[0] < this.currentLocation[0]) {  //Enemy on left side move this.player to enemy (horizontal + offset, vertical) Maplocation
            Locable newLoc = new Pair<Integer, Integer>(this.selectedMonster.currentLocation[0] + 50, this.selectedMonster.currentLocation[1]);
            this.movePlayer(newLoc);  //Moves this.player right of selectedMonster
            if (isAttacking && autoAttackCooldown.compareTo(serverCurrectTimestamp) > 2) {  //Checks if it has been 2 seconds since last auto-attack
                ArrayList<Selectable> enemiesInRange = this.server.enemiesInGridBaseAttackRange(this.currentLocation);  //Finds enemies within attack range of base attack range
                if (enemiesInRange.contains(this.selectedMonster)) {    //Attacks enemy if found in range
                    autoAttackCooldown = new Timestamp(Server.currentTimeMillis()); //Updates new "cooldown" for auto-attack
                    attackMonster(this.selectedMonster, 1);  //Attacks monster, sends to server
                }
            }
        }
        else if (this.selectedMonster.currentLocation[0] > this.currentLocation[0]) {  //Enemy on right side move this.player to enemy (horizontal - offset, vertical) Maplocation
            Locable newLoc = new Pair<Integer, Integer>(this.selectedMonster.currentLocation[0] - 50, this.selectedMonster.currentLocation[1]);
            this.movePlayer(newLoc);   //Moves this.player left of selectedMonster
                if (isAttacking && autoAttackCooldown.compareTo(serverCurrectTimestamp) > 2) {  //Checks if it has been 2 seconds since last auto-attack
                    ArrayList<Selectable> enemiesInRange = this.server.enemiesInGridBaseAttackRange(this.currentLocation);  //Finds enemies within attack range of base attack range
                    if (enemiesInRange.contains(this.selectedMonster)) {    //Attacks enemy if found in range
                        autoAttackCooldown = new Timestamp(Server.currentTimeMillis()); //Updates new "cooldown" for auto-attack
                        attackMonster(this.selectedMonster, 1);  //Attacks monster, sends to server
                    }
                }
        }
        else       // this.player and enermy on same horizontal location
            null; //Needs testing
    }

    private void selectMonster (Selectable monster){
        //Grabs player info from already loaded players or from the parent server then updates this.selectedMonster. Monster info is loaded with the map
    }

    public Selectable attackMonster (Selectable selectedMonster,byte skill){ //Sends attackEvent to server
        return null; //Returns monster being attacked with what skill and stats of this.player
    }

    public void movePlayer (Locable newLocation){
        //traces from currentlocation to newlocation
    }
}

