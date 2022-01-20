var speed = 5;

/* Call this function with a string containing the ID name to
 * the element containing the number you want to do a count animation on.*/
function incEltNbr() {
  var elementi;
  $(".fa-building").map(function(){
    elt = this;
    endNbr = this.innerHTML;
    //(document.getElementById(id).innerHTML);
    incNbrRec(0, endNbr, elt);
  })

}

/*A recursive function to increase the number.*/
function incNbrRec(i, endNbr, elt) {
  if (i <= endNbr) {
    elt.innerHTML = i;
    setTimeout(function() {//Delay a bit before calling the function again.
      incNbrRec(i + 1, endNbr, elt);
    }, speed);
  }
}

/*Function called on button click*/
function incNbr(){
  incEltNbr();
}

$(document).ready(function () {
  incEltNbr();
});
