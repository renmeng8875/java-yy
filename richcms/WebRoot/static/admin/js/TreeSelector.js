//创建TreeSelector对象
function TreeSelector(settings) 
{
	this._htmlTemplate = [];
	this._container = document.getElementById(settings.containerId)||document.body;
	this._name = settings.selectName||"_treeSelector_name";
	this._id = settings.selectId||"_treeSelector_id";
	this._value = settings.optionValue;
	this._text = settings.optionText;
	this._level = settings.selectLevel;
	this._subSet = settings.subSet;
	this._selectedId = settings.selectedId;
	this._isMultiSelect=settings.isMultiSelect||false;
	this._size=settings.size;
	this._defaultOptionValue=settings.defaultOptionValue;
	this._defaultOptionText=settings.defaultOptionText;
	this._style=settings.style;
	this.buildTreeTemplate(settings.data);
	this.onChange=settings.onChange||null;
	this.createTree();
}

TreeSelector.prototype.buildTreeTemplate = function(data)
{
	var _option = '<option ';
	if(data[this._value]==this._selectedId && this._defaultOptionValue==undefined && this._defaultOptionText==undefined){
		_option += 'selected';
	}
	var _hasChildren=data[this._subSet]&&data[this._subSet].length>0?true:false;
	if(_hasChildren){
		_option += ' data-haschild="1"';
	}else{
		_option += ' data-haschild="0"';
	}
	_option += ' value="'+data[this._value]+'">'+this.getLevelstr(data[this._level])+data[this._text]+'</option>';
	this._htmlTemplate.push(_option);
	
	if(_hasChildren)
	{
		for(var i=0;i<data[this._subSet].length;i++)
		{ 
			if(data[this._subSet][i][this._subSet]&&data[this._subSet][i][this._subSet].length>0)
			{
				this.buildTreeTemplate(data[this._subSet][i]);	
			}else{
				_option = '<option ';
				if(data[this._subSet][i][this._value]==this._selectedId){
					_option += 'selected';
				}
				_option += ' data-haschild="0"  value="'+data[this._subSet][i][this._value]+'">'+this.getLevelstr(data[this._subSet][i][this._level])+data[this._subSet][i][this._text]+'</option>';
				this._htmlTemplate.push(_option);	
			}
		}
	}
}

TreeSelector.prototype.createTree = function(){
	var str = '<select  id="'+this._id+'" name="'+this._name+'"';
	if(this._isMultiSelect){
		str+=' multiple="multiple" ';
	}
	if(this._size){
		str+=' size="'+size+'"';
	}
	if(this._style){
		str+=' style="'+this._style+'"';
	}
	str+='>';
	
	var defaultOption='';
	if(this._defaultOptionValue!=undefined && this._defaultOptionText!=undefined){
		defaultOption='<option value='+this._defaultOptionValue+' selected>'+this._defaultOptionText+'</option>';
		str+=defaultOption;
	}
	str+=this._htmlTemplate.join("")+'</select>';
	this._container.innerHTML = str;
	var selectobj = $("#"+this._id);
	selectobj.bind("change",this.onChange);
	
}

TreeSelector.prototype.getLevelstr = function(level){
	var level = parseInt(level);
	if(level==0) return "";
	var levelstr = [];
	for(var i=0;i<level;i++)
	{
		levelstr.push("&nbsp;&nbsp;|");
	}
	levelstr.push("--");
	return levelstr.join("");
}

