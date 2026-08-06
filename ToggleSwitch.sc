ToggleSwitch :  SCViewHolder {

	var  <value=0,<>font,<toggleSize,<>labelOffset=5,<>radius=\auto,<>knobInset=3,<>label,<>label2,<>offColor,<>drawRect, <>onColor,<>borderColor,<>drawFunc,toggleRect,<>border = 2;

	*new { arg parent, bounds, label,toggleSize=40,argLabel2,radius=\auto;
		^super.new.init(parent, bounds,label,toggleSize,argLabel2,radius);
	}
	*newSquare { arg parent, bounds, label,toggleSize=40,argLabel2,radius=0;
		^super.new.init(parent, bounds,label,toggleSize,argLabel2,radius).knobInset_(0);
	}


	init { |argParent, argBounds,argLabel,argToggleWidth,argLabel2,argRadius|
		label = argLabel;
		label2 = argLabel2;
		radius = argRadius;
		toggleSize=argToggleWidth;
		argBounds=argBounds.asRect;
		this.view = UserView(argParent, argBounds);
		this.view.mouseDownAction={ arg v, x, y, modifiers, buttonNumber, clickCount;
			this.mouseDown(x, y, modifiers, buttonNumber, clickCount)};
		drawRect = this.view.bounds.moveTo(0,0);

		borderColor=Color.grey;
		drawFunc = {arg uview; this.drawWidget(uview)};
		view.drawFunc= {arg uview; drawFunc.value(uview)};
		onColor=Color.green(0.6);
		offColor=Color.grey.alpha_(0.6);
		font = font? Font.default;
		view.drawFunc(this.view);
	}

	drawWidget{|uview|
		var border=1,enabled=1,labelOffset=10, rad,orientation;

		orientation=(drawRect.width>drawRect.height).if{\horz}{\vert};
		toggleRect = this.pr_calcToggleRect(orientation);

		rad = this.pr_calcRadius() ;

		Pen.use {
			// horizontal
			labelOffset=5;
			Pen.font = font;
			Pen.color_(Color.black);
			label.notNil.if{
				this.drawLabel(orientation);
			};
			label2.notNil.if{
				this.drawLabel2(orientation);
			};

			this.drawKnob(orientation,rad);

		};
		this.drawFrame(orientation,rad);



	}

	pr_calcStringRect{|draw=\horz|
		var horz = draw==\horz;
		horz.if{
			^Rect(0,0,this.drawBounds.width-toggleSize-labelOffset,drawRect.height).anchorTo((toggleRect.left-labelOffset)@toggleRect.top,\topRight);
		}{
			^Rect(0,0,this.drawBounds.width,min(2*(font.size?10),20)).moveTo(0,toggleRect.bottom);
		}

	}
		pr_calcStringRect2{|draw=\horz|
		var horz = draw==\horz;
		horz.if{
			^Rect(this.drawBounds.right,0,this.drawBounds.width-toggleSize-labelOffset,drawRect.height).anchorTo((toggleRect.right+labelOffset)@toggleRect.top,\topLeft);
		}{
			^Rect(0,this.drawBounds.top,this.drawBounds.width,min(2*(font.size?10),20)).moveTo(0,toggleRect.bottom).anchorTo(toggleRect.left@(toggleRect.top),\bottomLeft);
		}

	}


	pr_calcToggleRect{|draw=\horz|
		var horz = draw==\horz, moveBy;
		moveBy = label2.isNil.if{0}{(this.drawBounds.width-toggleSize)*0.5.neg};
		horz.if{{};
			^drawRect.moveBy(this.drawBounds.width-toggleSize,0).width_(toggleSize).moveBy(moveBy,0);
		}{
			moveBy = label2.isNil.if{0}{(this.drawBounds.height-toggleSize)*0.5.neg};

			^drawRect.moveBy(0,this.drawBounds.height-toggleSize-min(2*(font.size?10),20)).height_(toggleSize);
		}
	}

	pr_togOnRect{|draw=\horz|
		var horz = draw==\horz;
		horz.if{
			^Rect(toggleRect.right-toggleRect.height,0,toggleRect.height,toggleRect.height).insetBy(knobInset,knobInset);
		}{
			^Rect(0,toggleRect.top,toggleRect.width,toggleRect.width).insetBy(knobInset,knobInset);
		}

	}

	pr_togOffRect{|draw=\horz|
		var horz = draw==\horz;
		horz.if{
			^Rect(toggleRect.left,0,toggleRect.height,toggleRect.height).insetBy(knobInset,knobInset);
		}{
			^Rect(0,toggleRect.bottom-toggleRect.width,toggleRect.width,toggleRect.width).insetBy(knobInset,knobInset);
		}

	}

	pr_calcRadius{arg defaultRad;
		(radius==\auto).if{^drawRect.width.min( drawRect.height )}{^radius};
	}

	drawLabel{
		|draw=\horz|
		var horz = draw==\horz;
		horz.if{
			Pen.stringRightJustIn( label,this.pr_calcStringRect(\horz));
		}{
			Pen.stringCenteredIn( label,this.pr_calcStringRect((\vert)));
		}
	}

	drawLabel2{
		|draw=\horz|
		var horz = draw==\horz;
		horz.if{
			Pen.stringLeftJustIn( label2,this.pr_calcStringRect2(\horz));
		}{
			Pen.stringCenteredIn( label2,this.pr_calcStringRect2(\vert));
		}
	}

	drawKnob{|orientation,rad|
		value.booleanValue.if{
			Pen.roundedRect(this.pr_togOnRect(orientation),rad);
			onColor.fill;
		}{
			Pen.roundedRect(this.pr_togOffRect(orientation),rad);
			offColor.fill;
		};
	}

	drawFrame{|orientation,rad|
		Pen.strokeColor_(borderColor);
		Pen.roundedRect( toggleRect.insetBy( border/2,border/2 ), rad - (border/2)).stroke;
	}

	value_ { arg val;
		value=val;
		this.view.refresh;
	}

	valueAction_ { arg val;
		this.value_(val);
		view.action.value(this)
	}

	mouseDown{ arg x, y, modifiers, buttonNumber, clickCount;
		var newVal;
		this.valueAction_((value-1).abs);

	}


}
