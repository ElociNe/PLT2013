object AExp {
  abstract class AExp

  case class Num(int : Int) extends AExp
  case class Add(lhs : AExp, rhs : AExp) extends AExp
  case class Mul(lhs : AExp, rhs : AExp) extends AExp
  case class Var(sym : Symbol) extends AExp
  // In functional style, extending the language with new data (classes) causes
  // changes to all existing operations (methods).
  case class Sub(lhs : AExp, rhs : AExp) extends AExp
  case class Div(lhs : AExp, rhs : AExp) extends AExp

  implicit def toNum(int : Int) : AExp = Num(int)
  implicit def toVar(sym : Symbol) : AExp = Var(sym)

  type Env = Map[Symbol, Int]

  def evaluate(exp : AExp, env : Env) : Int = exp match {
    case Num(int) => int
    case Add(lhs, rhs) => evaluate(lhs, env) + evaluate(rhs, env)
    case Mul(lhs, rhs) => evaluate(lhs, env) * evaluate(rhs, env)
    case Var(sym) => env(sym)
    // caused changes
    case Sub(lhs, rhs) => evaluate(lhs, env) - evaluate(rhs, env)
    case Div(lhs, rhs) => {
      val divisor : Int = evaluate(rhs, env)
      if (divisor == 0)
        sys.error("Dividing by zero: " + print(exp))
      else
        evaluate(lhs, env) / divisor
    }
  }

  def print(exp : AExp) = {
    def pretty(sup : Int, exp : AExp) : String = {
      def group(sub : Int, lhs : AExp, bop : Symbol, rhs : AExp) = {
        val ppe = pretty(sub, lhs) + " " + bop.name + " " + pretty(sub, rhs)
        if (sub < sup) "(" + ppe + ")" else ppe
      }
      exp match {
        case Num(int) => int.toString
        case Add(lhs, rhs) => group(1, lhs, '+, rhs) 
        case Mul(lhs, rhs) => group(2, lhs, '*, rhs)
        case Var(sym) => sym.name
        // caused changes
        case Sub(lhs, rhs) => group(1, lhs, '-, rhs)
        case Div(lhs, rhs) => group(2, lhs, '/, rhs)
      }
    }
    pretty(0, exp)
  }
}

