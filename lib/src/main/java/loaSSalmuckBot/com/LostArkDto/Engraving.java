package loaSSalmuckBot.com.LostArkDto;

import lombok.Getter;

@Getter
public enum Engraving {
//	adrenaline("아드레날린",new double[]{ 1.8f,3.6f,6f}),
	allOutAttack("속전속결",new double[]{ 0.0,0.0,0.0}),
	awakening("각성",new double[]{ 0.0,0.0,0.0}),
	necromancy("강령술",new double[]{ 0.0,0.0,0.0}),
	enhancedShield("강화 방패",new double[]{ 0.0,0.0,0.0}),
	masterBrawler("결투의 대가",new double[]{ 0.0,0.0,0.0}),
	dropsOfEther("구슬동자",new double[]{ 0.0,0.0,0.0}),
	strongWill("굳은 의지",new double[]{ 0.0,0.0,0.0}),
	vitalPointStrike("급소 타격",new double[]{ 0.0,0.0,0.0}),
	masterOfAmbush("기습의 대가",new double[]{ 0.0,0.0,0.0}),
	emergencyRescue("긴급 구조",new double[]{ 0.0,0.0,0.0}),
	masterTenacity("달인의 저력",new double[]{ 0.0,0.0,0.0}),
	raidCaptain("돌격 대장",new double[]{ 0.0,0.0,0.0}),
	magickStream("마나의 흐름",new double[]{ 0.0,0.0,0.0}),
	barricade("바리케이트",new double[]{ 0.0,0.0,0.0}),
	lightningFury("번개의 분노",new double[]{ 0.0,0.0,0.0}),
	brokenBone("부러진 뼈",new double[]{ 0.0,0.0,0.0}),
	crushingFist("분쇄의 주먹",new double[]{ 0.0,0.0,0.0}),
	fortitude("불굴",new double[]{ 0.0,0.0,0.0}),
	preemptiveStrike("선수필승",new double[]{ 0.0,0.0,0.0}),
	superCharge("슈퍼 차지",new double[]{ 0.0,0.0,0.0}),
	contender("승부사",new double[]{ 0.0,0.0,0.0}),
	sightFocus("시선 집중",new double[]{ 0.0,0.0,0.0}),
	shieldPiercing("실드 관통",new double[]{ 0.0,0.0,0.0}),
	stabilisedStatus("안정된 상태",new double[]{ 0.0,0.0,0.0}),
	disrespect("약자 무시",new double[]{ 0.0,0.0,0.0}),
	divineProtection("여신의 가호",new double[]{ 0.0,0.0,0.0}),
//	etherPredator("에테르 포식자",new double[]{ 6f,9f,15f}),
	keenBluntWeapon("예리한 둔기",new double[]{0.0,0.0,0.0}),
	Grudge("원한",new double[]{ 0.0,0.0,0.0}),
	cursedDoll("저주받은 인형",new double[]{ 3.0,8.0,16.0}),
	Expert("전문의",new double[]{ 0.0,0.0,0.0}),
	spiritAbsorption("정기 흡수",new double[]{ 0.0,0.0,0.0}),
	preciseDagger("정밀 단도",new double[]{ 0.0,0.0,0.0}),
	heavyArmor("중갑 착용",new double[]{ 0.0,0.0,0.0}),
	increaseMass("질량 증가",new double[]{ 4.0,10.0,18.0}),
	maxMPIncrease("최대 마나 증가",new double[]{ 0.0,0.0,0.0}),
	propulsion("추진력",new double[]{ 0.0,0.0,0.0}),
	hitMaster("타격의 대가",new double[]{ 0.0,0.0,0.0}),
	masterofEscape("탈출의 명수",new double[]{ 0.0,0.0,0.0}),
	explosiveExpert("중갑 착용",new double[]{ 0.0,0.0,0.0}),
	attackPowerDecrease("공격력 감소",new double[]{ -2.0,-4.0,-6.0}),
	attackSpeedDecrease("공격속도 감소",new double[]{ 0.0,0.0,0.0}),
	defenseDecrease("방어력 감소",new double[]{ 0.0,0.0,0.0}),
	speedDecrease("이동속도 감소",new double[]{ 0.0,0.0,0.0}),
	none("직업각인",new double[]{ 0.0,0.0,0.0});
	
	
	private final String name;
	private final double[] level;
	

	Engraving(String name, double[] level) {
		this.name = name;
		this.level = level;
		// TODO Auto-generated constructor stub
	}
	public static Engraving getEngravingByName(String engravingName) {
        for (Engraving engraving : Engraving.values()) {
            if (engraving.getName().equals(engravingName)) {
                return engraving;
            }
        }
        return none;
    }
}
