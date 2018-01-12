Array.prototype.unique = function() {
	var newArr = [];
	for ( var i = 0; i < this.length; i++) {
		if (newArr.indexOf(this[i]) == -1) {
			newArr.push(this[i]);
		}
	}
	return newArr;
};


