/**
 * JQuery扩展，主要是处理ajax异常和错误
 *
 * @author V1.0 (chengaochang@ceopen.cn)
 * @author V1.1 (wangjun1@yy.com)
 */
;
(function ($) {
    $.ajaxSetup({cache: false, traditional: true, contentType: "application/x-www-form-urlencoded; charset=UTF-8"});
    var ajax = $.ajax;
    var sendStateObj = {};
    var errorHandler = function (s, xhr) {
        var contentType = xhr.getResponseHeader("Content-Type");
        var data = xhr.responseText;

        try {
            data = eval("(" + data + ")");
        }
        catch (error) {
            data = {code: "400", msg: error};
        }

        var isHandler = false;
        //开发人员自定义异常处理
        if (s.exception) {
            s.exception(data);
            isHandler = true;
        }
        if (s.errorPage) {
            window.location.href = s.errorPage;
            isHandler = true;
            return isHandler;
        }
        if (s.autoAlert == true) {
            s.alert(data);
            isHandler = true;
        }
        return isHandler;
    };

    var hasJsonException = function (xhr) {
        return xhr.getResponseHeader("Json-Exception") == "jsExc";
    };

    $.ajax = function (s) {
        //是否等待此次请求完成再执行下次请求，防止重复提交
        if ($.trim(s.type).toLowerCase() == "post") {
            var sendState = sendStateObj[s.url];
            if (!sendState) {
                sendState = {};
                sendStateObj[s.url] = sendState;
            }
            //console.log("currentState.isSendbefore="+sendState.isSending);
            if (sendState.isSending) {
                return;
            }
            sendState.isSending = true;
            //console.log("currentState.send="+sendState.isSending);
            s.sendState = sendState;
        }
        ;

        //扩展ajax提交之前的操作
        var oldBeforeSend = s.beforeSend;
        s.beforeSend = function (xhr, s) {
            //设置是否显示遮罩效果
            if (s.showMask && top.commonShowMask) {
                top.commonShowMask();
            }
            var isSend = true;
            try {
                xhr.setRequestHeader("Request-Type", "ajax");
            } catch (e) {
            }
            if (oldBeforeSend) {
                isSend = oldBeforeSend(xhr, s);
            }
            return isSend;
        };

        //扩展成功提示
        var oldSuccess = s.success;
        s.success = function (data, status, xhr) {
            if (hasJsonException(xhr)) {
                errorHandler(s, xhr);
            } else {
                if (s.autoAlert == true && s.dataType == "json" && data && data.code != 0 && data.msg) {
					s.alert(data);
                    return;
                }
                //开发人员自定义成功处理
                if (oldSuccess) {
                    oldSuccess(data, status, xhr);
                }
            }
        };

        var oldComplete = s.complete;
        s.complete = function (xhr, textStatus) {
            if (s.sendState) {
                s.sendState.isSending = false;
            }

            if (oldComplete) {
                oldComplete(xhr, textStatus);
            }
        };
        var oldError = s.error;
        s.error = function (xhr, textStatus, errorThrown) {
            if (xhr.status >= 500) {
                if (s.errorPage) {
                    window.location.href = s.errorPage;
                    return;
                }

                if (hasJsonException(xhr) && errorHandler(s, xhr)) {
                    return;
                }
            }
            if (oldError) {
                oldError(xhr, textStatus, errorThrown);
            }
        };
        s.alert = function (data) {
            // 统一处理异常
            if (data.code == 1001) {
					$.alert("" + data.msg,"error",{fn:function(index){
						showLoginBox();
					}});
            } else {
					$.alert("未知类型异常:" + data.msg);
            }
        };
        ajax(s);
    };
    
    var checkFn=function($this,options){
    	var checks=$this.attr('check');
		var arr=checks.split(',');
		var msgs=$this.attr('msg').split(",");
		
		//是否缓存较验器
		var validatorCache=$this.attr('validatorCache');
		var isCache=true;
		if(validatorCache==0){
			isCache=false;
		}
		
		var validators=$this.data('validators');
		//不缓存或第一次的初始化
		if(!isCache||!validators){
			var msg;
			validators=[];
			for(var i=0;i<arr.length;i++){
				var check=arr[i];
				if(!check){continue;}
				//有括号的特殊处理
				var index=check.indexOf("(");
				var fun;
				if(index!=-1){
					fun=$.reg[check.substr(0,index)];
				}else{
					fun=$.reg[check]
				}
				if(!fun){
					msg="异常的检查器";
					fun=$.reg.__error__;
				}else{
					var result;
					//有括号的特殊处理
					if(index!=-1){
						eval("fun=function(value){return $.reg."+check.substr(0,index)+"(value,"+check.substr(index+1)+"}");
					}
					msg=msgs[i];
				}
				validators.push([fun,msg]);
			}
			//缓存
			if(isCache)$this.data('validators',validators);
		}
		
		//正式较验
		for(var i=0;i<validators.length;i++){
			var validator=validators[i];
			var result=validator[0]($this.val());
			if(!result){
				//$this.focus();
				if(options.handleError)options.handleError($this,validator[1]);
				return false;
			}
		}
		if(options.handleOk)options.handleOk($this);
		return true;
    }
    
    
    $.fn.extend({
		/**
		 * ajax提交表单
		 * 
		 * @param formId要提交的表单id,options参数:validateFn为验证参数返回错误提示信息,$.ajax参数
		 */
		ajaxForm : function(formId,options) {
			var settings={isBlurCheck:true};
			$.extend(settings, options);
			var $form=$("#"+formId);
			if(settings.isBlurCheck){
				$form.find("[check]").live('blur',function(){
					checkFn($(this),settings);
				});
			}
			
			$(this).each(function(index){
				$(this).click(function(){
					$.ajaxForm(formId,settings);
				});
			});
		}
    });
    
    $.extend({
    	ajaxForm : function(formId,options) {
			var $form=$("#"+formId);
			var settings = {
					data:$form.serialize(),
					cache:false,
					url:$form.attr("action"),
					type:$form.attr("method")||'POST',
					dataType : $form.attr("dataType")||'json',
					//是否弹出错误提示框
					autoAlert:true,
					//验证通过是否提交表单
					isSubmit:true,
					//提交表单前验证
					isDefaultCheck:true,
					//blur时添加事件
					isBlurCheck:true,
					//开发自定义验证,不通过返回false
					validateFn:function(data){return true},
					//验证失败时会执行
					handleError:function($this,msg){},
					//验证成功时会执行
					handleOk:function($this){},
					//验证所有完时会执行
					handleOver:function(flag,settings){}
			};
			$.extend(settings, options);
			
			//开发自定义验证
			var v=settings.validateFn($form.serializeArray());
			if(!v){
				settings.handleOver(false,settings);
				return;
			}
			
			//默认检测输入有效性
			var isOK=true;
			if(settings.isDefaultCheck){
				$form.find("[check]").each(function(){
					var $this=$(this);
					var r=checkFn($this,settings);
					if(!r){
						isOK=false;
					}
				});
			}
			settings.handleOver(isOK,settings);
			if(isOK&&settings.isSubmit){
				//正常提交
				$.ajax(settings);
			}
		},
		ajaxData : function(url,data,options) {
			var settings = {
					data:data,
					url:url,
					type:'POST',
					dataType:'json',
					cache:false,
					autoAlert:true
			};
			$.extend(settings, options);
			$.ajax(settings);
		},
		reg:{
			__error__:function(v){return false;},
			notBlank:function(v){return !/^\s*$/.test(v);},
			number:function(v){return /^\d+$/.test(v)},
			email:function(v){return /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/.test(v)},
			chinese:function(v){return /^[\u2E80-\u9FFF]+$/.test(v)},
			maxLength:function(v,l){return $.chineseLength(v)<=l},
			minLength:function(v,l){return $.chineseLength(v)>=l}
		},
		//type:alert,success,error,ask
		alert:function(msg,type,options){
			if ($.messagebox) {
				var settings={type: type||"error", msg: msg};
				$.extend(settings, options);
				$.messagebox(settings);
			} else {
				alert(msg);
			}
		},
		englishLength:function(str){
			if(str==null){return 0;}
			var v=str;
			if(typeof str !='string'){
				v=str+'';
			}
			var length=v.length;
			var result=length;
			//>256的加1
			for(var i=0; i<length; i++) { 
				if ((v.charCodeAt(i) & 0xff00)!=0)result++; 
			} 
			return result;
		},
		chineseLength:function(str){
			return ($.englishLength(str)+1)>>1;
		}
		,
		validateMultiForm : function(element ,option){			
		    var b = true ; 
		    var forms = $(element) ;
		    //遍历每个form
		    $(forms).each(function(i , $form){
		    	//遍历form里面的有check属性的元素
		       var checks = $($form).find("[check]") ;		    
		    	   checks.each(function(){
						var $this=$(this);
						b = checkFn($this,option);
						if(!b){
						  return false ;	
						}					
					});
		      //如果b为真，继续自定义的校验 
			  if(b && typeof option.selfValitor !="undefined"){
		    	  b =  option.selfValitor(i , $($form)) ;
		       }		       
			   if(!b){
					  return false ;	
				}				
		    }) ;    
		    return b  ;
		}		
    });
})(jQuery);