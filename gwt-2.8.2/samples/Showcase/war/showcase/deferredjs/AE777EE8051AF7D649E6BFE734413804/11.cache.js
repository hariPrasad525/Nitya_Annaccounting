$wnd.showcase.runAsyncCallback11("function wEb(){}\nfunction yEb(){}\nfunction rEb(a,b){a.b=b}\nfunction sEb(a){if(a==hEb){return true}ez();return a==kEb}\nfunction tEb(a){if(a==gEb){return true}ez();return a==fEb}\nfunction xEb(a){this.b=(_Fb(),WFb).a;this.e=(eGb(),dGb).a;this.a=a}\nfunction pEb(a,b){var c;c=dC(a.fb,153);c.b=b.a;!!c.d&&jzb(c.d,b)}\nfunction qEb(a,b){var c;c=dC(a.fb,153);c.e=b.a;!!c.d&&lzb(c.d,b)}\nfunction lEb(){lEb=EX;eEb=new wEb;hEb=new wEb;gEb=new wEb;fEb=new wEb;iEb=new wEb;jEb=new wEb;kEb=new wEb}\nfunction uEb(){lEb();nzb.call(this);this.b=(_Fb(),WFb);this.c=(eGb(),dGb);(Zvb(),this.e)[vac]=0;this.e[wac]=0}\nfunction mEb(a,b,c){var d;if(c==eEb){if(b==a.a){return}else if(a.a){throw $W(new fXb('Only one CENTER widget may be added'))}}Rh(b);mQb(a.j,b);c==eEb&&(a.a=b);d=new xEb(c);b.fb=d;pEb(b,a.b);qEb(b,a.c);oEb(a);Th(b,a)}\nfunction nEb(a){var b,c,d,e,f,g,h;VPb((Zvb(),a.hb),'',ccc);g=new Q2b;h=new wQb(a.j);while(h.b<h.c.c){b=uQb(h);f=dC(b.fb,153).a;d=dC(YZb(g3b(g.d,f)),84);c=!d?1:d.a;e=f==iEb?'north'+c:f==jEb?'south'+c:f==kEb?'west'+c:f==fEb?'east'+c:f==hEb?'linestart'+c:f==gEb?'lineend'+c:a9b;VPb(Qo(b.hb),ccc,e);i$b(g,f,sXb(c+1))}}\nfunction oEb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r;b=(Zvb(),a.d);while(Bxb(b)>0){wo(b,Axb(b,0))}o=1;e=1;for(i=new wQb(a.j);i.b<i.c.c;){d=uQb(i);f=dC(d.fb,153).a;f==iEb||f==jEb?++o:(f==fEb||f==kEb||f==hEb||f==gEb)&&++e}p=mB(wR,w6b,263,o,0,1);for(g=0;g<o;++g){p[g]=new yEb;p[g].b=$doc.createElement(tac);so(b,ewb(p[g].b))}k=0;l=e-1;m=0;q=o-1;c=null;for(h=new wQb(a.j);h.b<h.c.c;){d=uQb(h);j=dC(d.fb,153);r=$doc.createElement(uac);j.d=r;j.d[iac]=j.b;j.d.style[jac]=j.e;j.d[O6b]=j.f;j.d[N6b]=j.c;if(j.a==iEb){awb(p[m].b,r,p[m].a);so(r,ewb(d.hb));r[jbc]=l-k+1;++m}else if(j.a==jEb){awb(p[q].b,r,p[q].a);so(r,ewb(d.hb));r[jbc]=l-k+1;--q}else if(j.a==eEb){c=r}else if(sEb(j.a)){n=p[m];awb(n.b,r,n.a++);so(r,ewb(d.hb));r[dcc]=q-m+1;++k}else if(tEb(j.a)){n=p[m];awb(n.b,r,n.a);so(r,ewb(d.hb));r[dcc]=q-m+1;--l}}if(a.a){n=p[m];awb(n.b,c,n.a);so(c,ewb(eh(a.a)))}}\nvar ccc='cwDockPanel';DX(418,1,_8b);_.Bc=function ueb(){var a,b,c;XZ(this.a,(a=new uEb,(Zvb(),a.hb).className='cw-DockPanel',a.e[vac]=4,rEb(a,(_Fb(),VFb)),mEb(a,new TCb(Ybc),(lEb(),iEb)),mEb(a,new TCb(Zbc),jEb),mEb(a,new TCb($bc),fEb),mEb(a,new TCb(_bc),kEb),mEb(a,new TCb(acc),iEb),mEb(a,new TCb(bcc),jEb),b=new TCb('\\u8FD9\\u4E2A\\u793A\\u4F8B\\u4E2D\\u5728<code>DockPanel<\\/code> \\u7684\\u4E2D\\u95F4\\u4F4D\\u7F6E\\u6709\\u4E00\\u4E2A<code>ScrollPanel<\\/code>\\u3002\\u5982\\u679C\\u5728\\u4E2D\\u95F4\\u653E\\u5165\\u5F88\\u591A\\u5185\\u5BB9\\uFF0C\\u5B83\\u5C31\\u4F1A\\u53D8\\u6210\\u9875\\u9762\\u5185\\u7684\\u53EF\\u6EDA\\u52A8\\u533A\\u57DF\\uFF0C\\u65E0\\u9700\\u4F7F\\u7528IFRAME\\u3002<br><br>\\u6B64\\u5904\\u4F7F\\u7528\\u4E86\\u76F8\\u5F53\\u591A\\u65E0\\u610F\\u4E49\\u7684\\u6587\\u5B57\\uFF0C\\u4E3B\\u8981\\u662F\\u4E3A\\u4E86\\u53EF\\u4EE5\\u6EDA\\u52A8\\u81F3\\u53EF\\u89C6\\u533A\\u57DF\\u7684\\u5E95\\u90E8\\u3002\\u5426\\u5219\\uFF0C\\u60A8\\u6050\\u6015\\u4E0D\\u5F97\\u4E0D\\u628A\\u5B83\\u7F29\\u5230\\u5F88\\u5C0F\\u624D\\u80FD\\u770B\\u5230\\u90A3\\u5C0F\\u5DE7\\u7684\\u6EDA\\u52A8\\u6761\\u3002'),c=new mAb(b),c.hb.style[O6b]='400px',c.hb.style[N6b]='100px',mEb(a,c,eEb),nEb(a),a))};DX(873,255,T6b,uEb);_.gc=function vEb(a){var b;b=hyb(this,a);if(b){a==this.a&&(this.a=null);oEb(this)}return b};var eEb,fEb,gEb,hEb,iEb,jEb,kEb;var xR=LWb(R6b,'DockPanel',873);DX(152,1,{},wEb);var uR=LWb(R6b,'DockPanel/DockLayoutConstant',152);DX(153,1,{153:1},xEb);_.c='';_.f='';var vR=LWb(R6b,'DockPanel/LayoutData',153);DX(263,1,{263:1},yEb);_.a=0;var wR=LWb(R6b,'DockPanel/TmpRow',263);b6b(zl)(11);\n//# sourceURL=showcase-11.js\n")
