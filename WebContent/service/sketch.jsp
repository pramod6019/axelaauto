<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Sketch" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
<link REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css"/>
<link REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css"/>
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>

<script type="text/javascript" src="../Library/canvas/javascript/ajax_site.js"></script>
<script type="text/javascript" src="../Library/canvas/javascript/sketch.js"></script>
<script type="text/javascript" src="../Library/canvas/javascript/desenho-min.js"></script>
<script type="text/javascript" src="../Library/canvas/javascript/animacao.js">
//background-image:url('../Library/canvas/imgs/room/sprites1.PNG');
//background-image:url('../../media/jobcardimg/jcimg_79.jpg'); 
//background-image:url(../Library/canvas/imgs/room/logo_room.png);
//background-image: url("../imgs/room/fundo.png");//----CSS

</script>
<link href="../Library/canvas/style/estilo_prototipo.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#canvas {
	float: left;
	z-index:2000;
	background-color: #FFF;
	background-repeat:no-repeat;
	background-position:center;
}
#canvasPrev {
	float: left;
	position: relative;
	margin-top: -334px;
	background-image:url('');
	background-repeat:no-repeat;
	background-position:center;
	background-color:transparent;
}
</style>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
    <td align="left"><%=mybean.LinkHeader%></td>
    <td valign="top"><form name="form1"  method="post">
        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tableborder" >
          <tr>
            <td>
                                 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"  class="listtable">
                <tr align="center">
                  <td><span id="carregando">Loading...</span>
                    <div id="fundo"></div>
                    <div id="borracha"></div>
                    <div id="janela">
                      <div class="titulo">
                        <div id="janela_titulo" style="float: left">Teste</div>
                        <div style="float: right"><a href="javascript:fecharJanela()">Close</a></div>
                      </div>
                      <div id="janela_conteudo" style="padding: 4px; font-size: 10px; font-weight: normal; color: #666;"> </div>
                    </div>
                    <div id="tela" style="margin-top: 100px;">
                      <div id="contorno">
                        <div style="float: left;">
                          <div id="desenho" style="background-color: gray;">
                            <div id="texto_alerta"></div>
                            <div id="tit_desenho" class="titulo" style="background-color: white;">Drawing</div>
                            <div id="opcoes">
                              <div id="opcoes_vez">
                                <div>
                                  <div id="op_0" class="op" style="margin-bottom: 2px; background-image:url('../Library/canvas/imgs/room/op_lapis.gif');" onclick="sel_opcao(0)" title="Pencil"> </div>
                                  <div id="op_1" class="op" style="margin-bottom: 2px; margin-left: 2px; background-image:url('../Library/canvas/imgs/room/op_borracha.gif');" onclick="sel_opcao(1)" title="Eraser"> </div>
                                </div>
                                <div>
                                  <div id="op_2" class="op" style="margin-bottom: 2px; background-image:url('../Library/canvas/imgs/room/op_quadrado.gif');" onclick="sel_opcao(2)" title="Rectangle"> </div>
                                  <div id="op_3" class="op" style="margin-bottom: 2px; margin-left: 2px; background-image: url('../Library/canvas/imgs/room/op_quadrado_borda.gif');" onclick="sel_opcao(3)" title="Rectangle Border"> </div>
                                </div>
                                <div>
                                  <div id="op_4" class="op" style="margin-bottom: 2px; background-image:url('../Library/canvas/imgs/room/op_circulo.gif');" onclick="sel_opcao(4)" title="Ellipse"> </div>
                                  <div id="op_5" class="op" style="margin-bottom: 2px; margin-left: 2px; background-image: url('../Library/canvas/imgs/room/op_circulo_borda.gif');" onclick="sel_opcao(5)" title="Ellipse Border"> </div>
                                </div>
                                <div>
                                  <div id="op_6" class="op" style="margin-bottom: 2px; background-image:url('../Library/canvas/imgs/room/op_linha.gif');" onclick="sel_opcao(6)" title="Line"> </div>
                                  <div id="op_7" class="op" style="margin-bottom: 2px; margin-left: 2px; background-image:url('../Library/canvas/imgs/room/op_balde.gif');" onclick="sel_opcao(7)" title="Flood Fill"> </div>
                                </div>
                                <div>
                                  <div id="op_8" class="op" style="margin-bottom: 2px; background-image:url('../Library/canvas/imgs/room/op_piker.gif');" onclick="sel_opcao(8)" title="Color Picker"> </div>
                                  <div id="novo" class="op" style="margin-bottom: 2px; margin-left: 2px; background-image:url('../Library/canvas/imgs/room/op_nova.gif');" onclick="sel_limpar()" title="Clear"> </div>
                                </div>
                                <div>
                                  <div id="op_desfazer" class="op" style="margin-bottom: 2px; background-image:url('../Library/canvas/imgs/room/op_desfazer.gif'); background-color: #CCC;" onclick="sel_desfazer()" title="Undo"> </div>
                                </div>
                              </div>
                            </div>
                            <div id="telaCanvas" style="float: right; line-height: 0; width: 501px; height: 334px; ">
                              <div id="areaCanvas" style="width: 501px; position: relative; " >
                                <canvas id="canvas" width="501" height="334" > </canvas>
                                <canvas id="canvasPrev"  width="501" height="334" > </canvas>
                                <script>
				            			var myCanvas = document.getElementById('canvas');
										var ctx = myCanvas.getContext('2d');
										var img = new Image();
										img.onload = function(){
											ctx.drawImage(img,0,0);
											};
									    img.src = '../Library/canvas/imgs/room/sprites1.png';
								</script> 
                              </div>
                            </div>
                            <div id="cores">
                              <div id="dica_dada"></div>
                              <div id="cores_vez">
                                <div id="cor_escolhida" class="op" style="border-color: black; float: left; background-color: #000;" title="Selected Color"></div>
                                <div style="float: left;">
                                  <div>
                                    <div class="cor" style="background-color: #000;" onclick="sel_cor(1)"></div>
                                    <div class="cor" style="background-color: #666;" onclick="sel_cor(2)"></div>
                                    <div class="cor" style="background-color: #8b0000;" onclick="sel_cor(3)"></div>
                                    <div class="cor" style="background-color: #008b00;" onclick="sel_cor(4)"></div>
                                    <div class="cor" style="background-color: #00008b;" onclick="sel_cor(5)"></div>
                                    <div class="cor" style="background-color: #008b8b;" onclick="sel_cor(6)"></div>
                                    <div class="cor" style="background-color: #8b8b00;" onclick="sel_cor(7)"></div>
                                    <div class="cor" style="background-color: #8B4500;" onclick="sel_cor(8)"></div>
                                    <div class="cor" style="background-color: #8B0A50;" onclick="sel_cor(9)"></div>
                                    <div class="cor" style="background-color: #551A8B;" onclick="sel_cor(10)"></div>
                                    <div class="cor" style="background-color: #548B54;" onclick="sel_cor(11)"></div>
                                    <div class="cor" style="background-color: #8B6969;" onclick="sel_cor(12)"></div>
                                    <div class="cor" style="background-color: #8B7B8B;" onclick="sel_cor(13)"></div>
                                  </div>
                                  <div style="clear: both;">
                                    <div class="cor" style="margin-top: 2px; background-color: #fff;" onclick="sel_cor(14)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #ccc;" onclick="sel_cor(15)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #ff0000;" onclick="sel_cor(16)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #00ff00;" onclick="sel_cor(17)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #0000ff;" onclick="sel_cor(18)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #00ffff;" onclick="sel_cor(19)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #ffff00;" onclick="sel_cor(20)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #FF7F00;" onclick="sel_cor(21)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #FF1493;" onclick="sel_cor(22)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #9B30FF;" onclick="sel_cor(23)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #9AFF9A;" onclick="sel_cor(24)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #FFC1C1;" onclick="sel_cor(25)"></div>
                                    <div class="cor" style="margin-top: 2px; background-color: #FFE1FF;" onclick="sel_cor(26)"></div>
                                  </div>
                                </div>
                                <div id="op_larg10" class="op" style="margin-left: 2px; float: right; background-image:url('../Library/canvas/imgs/room/op_larg10.gif');" onclick="sel_largura(10)" title="Weight 10"></div>
                                <div id="op_larg8" class="op" style="margin-left: 2px; float: right; background-image:url('../Library/canvas/imgs/room/op_larg8.gif');" onclick="sel_largura(8)" title="Weight 8"></div>
                                <div id="op_larg6" class="op" style="margin-left: 2px; float: right; background-image:url('../Library/canvas/imgs/room/op_larg6.gif');" onclick="sel_largura(6)" title="Weight 6"></div>
                                <div id="op_larg4" class="op" style="margin-left: 2px; float: right; background-image:url('../Library/canvas/imgs/room/op_larg4.gif');" onclick="sel_largura(4)" title="Weight 4"></div>
                                <div id="op_larg2" class="op" style="margin-left: 2px; float: right; background-image:url('../Library/canvas/imgs/room/op_larg2.gif');" onclick="sel_largura(2)" title="Weight 2"></div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div style="float: left;">
                          <div class="borda">
                            <div id="tit_desenho" class="titulo">Options</div>
                            <div style="padding: 5px; height: 83px; overflow: auto;">
                              <ul class="lista">
                                <li><a href="#" onclick="dimensao(); return false;">Attributes</a></li>
                                <li><a href="#" onclick="salvaDesenho(0); return false;">Save as PNG</a></li>
                                <li><a href="#" onclick="salvaDesenho(1); return false;">Save as JPG</a></li>
                                <li><a href="#" onclick="salvaDesenho(2); return false;">Save as GIF</a></li>
                              </ul>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <script src="../Library/canvas/javascript/ga.js" type="text/javascript"></script> 
                    <script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-3906902-7");
pageTracker._trackPageview();
} catch(err) {}</script></td>
                </tr>
              </table></td>
          </tr>
        </table>
      </form></td>
  </tr>
</TABLE>
</body>
</HTML>