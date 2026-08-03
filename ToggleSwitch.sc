ToggleSwitch :  SCViewHolder {

	var <>step, <value=0,<>font,<toggleWidth,<>labelOffset=10,<>knobInset=3,<>label,<>offColor,<>drawRect,<>action, <>onColor,<>frameColor,<>drawFunc,
	<>thumbSize=7;
	var <border = 2;
	var bgColor, <borderColor;

	*new { arg parent, bounds, label,toggleWidth=40;
		^super.new.init(parent, bounds,label,toggleWidth);
	}

	init { |argParent, argBounds,argLabel,argToggleWidth|
		label = argLabel;
		toggleWidth=argToggleWidth;
		argBounds=argBounds.asRect;
		this.view = UserView(argParent, argBounds);
		this.view.mouseDownAction={ arg v, x, y, modifiers, buttonNumber, clickCount;
			this.mouseDown(x, y, modifiers, buttonNumber, clickCount)};
		// this.keyDownAction = {arg v,char, modifiers, unicode, keycode, key;
		// this.q_defaultKeyDownAction(  char, modifiers, unicode, keycode, key) };
		drawRect = this.view.bounds.moveTo(0,0);

		frameColor=Color.grey;
		drawFunc = {arg uview; this.drawwidget(uview)};
		view.drawFunc= {arg uview; drawFunc.value(uview)};
		onColor=Color.green(0.6);
		offColor=Color.grey.alpha_(0.6);
		view.drawFunc(this.view);
	}


	drawwidget{|uview|
		var rect, localRadius,border=1,enabled=1;
		var toggleRect,radius;
		rect = drawRect;
		radius=12;
		radius = rect.width.min( rect.height )  ;
		font = font? Font.default;
		label.isNil.if{
			labelOffset = 0;
		};

		Pen.use {

			if(rect.width>rect.height){
				labelOffset=5;
				Pen.font = font;
				Pen.color_(Color.black);
				Pen.stringRightJustIn( label,Rect(0,0,this.drawBounds.width-toggleWidth-labelOffset,rect.height));

				toggleRect = (rect.moveBy(this.drawBounds.width-toggleWidth,0).width_(toggleWidth));

			value.booleanValue.not.if{
				Pen.circle(Rect(toggleRect.left,0,toggleRect.height,toggleRect.height).insetBy(knobInset,knobInset));
				offColor.fill;
			}{
				Pen.circle(Rect(toggleRect.right-toggleRect.height,0,toggleRect.height,toggleRect.height).insetBy(knobInset,knobInset));
				onColor.fill;
			};


			}{

				Pen.font = font;
				Pen.color_(Color.black);

				toggleRect = (rect.moveBy(0,this.drawBounds.height-toggleWidth-(labelOffset*2)).height_(toggleWidth));
				label.notNil.if{
					Pen.stringCenteredIn( label,Rect(0,0,this.drawBounds.width,(labelOffset*2)).moveTo(0,toggleRect.bottom));
				};
				value.booleanValue.not.if{
					Pen.circle(Rect(0,toggleRect.bottom-toggleRect.width,toggleRect.width,toggleRect.width).insetBy(knobInset,knobInset));
					offColor.fill;

				}{
					Pen.circle(Rect(0,toggleRect.top,toggleRect.width,toggleRect.width).insetBy(knobInset,knobInset));
					onColor.fill;

				};
			};
		};
		Pen.strokeColor_(frameColor);
		Pen.roundedRect( toggleRect.insetBy( border/2,border/2 ), radius -
			(border/2)
		).stroke;
/*
		enabled.booleanValue.not.if{
			Pen.use {
				Pen.fillColor = Color.white.alpha_(0.5);
				Pen.roundedRect( toggleRect, radius ).fill;
			};
		};*/

	}

	value_ { arg val;
		value=val;
		this.view.refresh;
	}

	valueAction_ { arg val;
		this.value_(val);
		this.doAction;
	}

	doAction { action.value(this) }


	mouseDown{ arg x, y, modifiers, buttonNumber, clickCount;
		var newVal;
		this.valueAction_((value-1).abs);

	}


}