package invaders.strategy;

import invaders.factory.Projectile;

public class NormalProjectileStrategy implements ProjectileStrategy{
    @Override
    public void update(Projectile p) {
        double newYPos = p.getPosition().getY() - 2;
        p.getPosition().setY(newYPos);
    }

    @Override
    public String getStrategy() {
        return "normal_straight";
    }
}
