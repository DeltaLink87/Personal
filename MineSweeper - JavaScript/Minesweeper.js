var tiles = [], mines = [];
var boardWidth, boardHeight;

var btnReset;
var lblMsg;

var winLose;

function init(){
	winLose = 0;
	
	boardWidth = 10;
	boardHeight = 10;
	
	for (var y = 0; y < boardHeight; y++){
		tiles.push([]);
		mines.push([]);
		for (var x = 0; x < boardWidth; x++){
			tiles[y].push(document.createElement("BUTTON"));
			setPosition(tiles[y][x], x*30 + "px", y*30 + "px", "30px", "30px");
			tiles[y][x].value = x + "" + y;
			tiles[y][x].onclick = function(){ return pressButton(this); };
			tiles[y][x].appendChild(document.createTextNode(""));
			
			if (Math.floor(Math.random() * 8) == 0)
				mines[y].push(true);
			else mines[y].push(false);
			
			document.body.appendChild(tiles[y][x]);
		}
	}
	
	btnReset = document.createElement("BUTTON");
	btnReset.appendChild(document.createTextNode("Reset"));
	setPosition(btnReset, ((boardWidth/2)*30-45) + "px", ((boardHeight)*30) + "px", "90px", "30px");
	btnReset.onclick = function(){ return reset(); };
	document.body.appendChild(btnReset);
	
	lblMsg = document.createElement("label");
	lblMsg.appendChild(document.createTextNode(""));
	setPosition(lblMsg, ((boardWidth*5/6)*30-45) + "px", ((boardHeight)*30) + "px", "90px", "30px")
	document.body.appendChild(lblMsg);
}

function pressButton(tile){
	if (winLose != 0) return;
	
	var coordinate = ~~tile.value;
	var x = Math.floor(coordinate / boardHeight), y = coordinate % boardHeight;
	
	if (tiles[y][x].childNodes[0].nodeValue == "")
		checkTile(x, y, true);
}

function checkTile(x, y, first){
	if (mines[y][x]){
		winLose = 1;
		tiles[y][x].childNodes[0].nodeValue = "M";
		lblMsg.childNodes[0].nodeValue = "You Lose";
	}
	else{
		var minesAround = 0;
		for (var y2 = -1; y2 < 2; y2++)
			for (var x2 = -1; x2 < 2; x2++){
				if (x2 == 0 && y2 == 0)	continue;
				if (x+x2 >= 0 && x+x2 < boardWidth && y+y2 >= 0 && y+y2 < boardHeight && mines[y+y2][x+x2])
					minesAround++;
			}
		tiles[y][x].childNodes[0].nodeValue = minesAround + "";
		
		if (minesAround == 0)
			for (var y2 = -1; y2 < 2; y2++)
				for (var x2 = -1; x2 < 2; x2++){
					if (x2 == 0 && y2 == 0) continue;
					if (x+x2 >= 0 && x+x2 < boardWidth && y+y2 >= 0 && y+y2 < boardHeight && tiles[y+y2][x+x2].childNodes[0].nodeValue == "")
						checkTile(x + x2, y + y2, false);
				}
		
		if (first)	checkWin();
	}
}

function checkWin(){
	var won = true;
	for (var y = 0; y < boardHeight && won; y++){
		for (var x = 0; x < boardWidth && won; x++){
			if (tiles[y][x].childNodes[0].nodeValue == "" && !mines[y][x])
				won = false;
		}
	}
	
	if (won){
		lblMsg.childNodes[0].nodeValue = "You Win";
		winLose = 2;
	}	
}

function reset(){
	winLose = 0;
	lblMsg.childNodes[0].nodeValue = "";
	
	for (var y = 0; y < boardHeight; y++){
		for (var x = 0; x < boardWidth; x++){
			tiles[y][x].childNodes[0].nodeValue = ""
			
			if (Math.floor(Math.random() * 8) == 0)
				mines[y][x] = true;
			else mines[y][x] = false;
		}
	}
}

function setPosition(thing, x, y, width, height){
	thing.style.position = "absolute";
	thing.style.left = x;
	thing.style.top = y;
	thing.style.width = width;
	thing.style.height = height;
}

init();